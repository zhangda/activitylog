package cn.rfidcn.activitylog.bolt;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import storm.trident.operation.Aggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.topology.TransactionAttempt;
import storm.trident.tuple.TridentTuple;
import backtype.storm.topology.FailedException;
import cn.rfidcn.activitylog.model.Activity;
import cn.rfidcn.activitylog.utils.ConfigReader;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class ActivityAggregator implements  Aggregator{

	static final Logger logger = Logger.getLogger(ActivityAggregator.class);
	
	private Connection mysqlConnection;
	private HTable hTable;
	private Queue<Activity> activityQueue = new ConcurrentLinkedQueue<Activity>();
	List<String> cols = Activity.getCols();
	
	static Configuration hbaseConfig = HBaseConfiguration.create();
	static {
		hbaseConfig.set("hbase.zookeeper.property.clientPort",  ConfigReader.getProperty("zkPort")); 
		hbaseConfig.set("hbase.zookeeper.quorum",  ConfigReader.getProperty("zkQuorum")); 
		hbaseConfig.set("hbase.master",  ConfigReader.getProperty("hMaster")); 
	}
	
	@Override
	public void prepare(Map conf, TridentOperationContext context) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				logger.error("ClassNotFoundException", e);
			}
			try {
				mysqlConnection = DriverManager.getConnection(ConfigReader.getProperty("mysqlConn"));
			} catch (SQLException e) {
				logger.error("SQLException", e);
			}
			
			try {
				hTable = new HTable(hbaseConfig, ConfigReader.getProperty("hbaseTablename"));
				hTable.setAutoFlush(false, true);
			} catch (IOException e) {
				logger.error("IOException", e);
			}
	}

	@Override
	public void cleanup() {
		try {
			mysqlConnection.close();
		} catch (SQLException e) {
			logger.error("SQLException", e);
		}
		try {
			hTable.close();
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		
	}

	@Override
	public Object init(Object batchId, TridentCollector collector) {
		return ((TransactionAttempt) batchId).getTransactionId();
	}

	@Override
	public void aggregate(Object val, TridentTuple tuple, TridentCollector collector) {
		Activity act = (Activity) tuple.get(0);
		act.setCountry((String)tuple.get(1));
		act.setState((String)tuple.get(2));
		act.setCity((String)tuple.get(3));
		act.setTxid((long) val);
		activityQueue.add(act);
	}

	@Override
	public void complete(Object val, TridentCollector collector) {
		if(activityQueue.size()==0) 
			return;
		logger.info("persist to database, # of records: " + activityQueue.size());
	
//		if(persistToMysql() && persistToHbase()){
			if(persistToHbase()){
			activityQueue.clear();
		}else{
			activityQueue.clear();
			throw new FailedException();
		}	
	}
	
	private boolean persistToMysql(){
		boolean flag = true;
		try {
			if(mysqlConnection == null || mysqlConnection.isClosed())
				mysqlConnection = DriverManager.getConnection(ConfigReader.getProperty("mysqlConn"));
			} catch(SQLException e){
				logger.error("SQLException", e);
				return false;
		}
			String sql = new StringBuilder().append("INSERT INTO ")
				.append(ConfigReader.getProperty("activityTable"))
				.append(" (")
				.append(Joiner.on(",").join(cols))
				.append(")")
				.append(" VALUES ")
				.append(placeHolder(cols.size()))
				.append(" ON DUPLICATE KEY UPDATE ")
				.append(updateHolder(cols))
				.toString();
		PreparedStatement ps =null;
		List<Method> methods = Activity.getters();
		try {
			int i=0;
			int batchSize = Integer.parseInt(ConfigReader.getProperty("mysqlBatchSize"));
			ps = mysqlConnection.prepareStatement(sql);
			for (final Activity act : activityQueue) {
				int j=0;
				for(Method m: methods){
					ps.setObject(++j, m.invoke(act));
				}
				ps.addBatch();
				if(++i %batchSize == 0)
					ps.executeBatch();
			}
			ps.executeBatch();
		} catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("Exception", e);
			flag = false;
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					logger.error("SQLException", ex);
					flag = false;
				}
			}
		}
		return flag;	
	}
	
	private boolean persistToHbase(){
		if(hTable ==null ){
			try {
				hTable = new HTable(hbaseConfig, ConfigReader.getProperty("hbaseTablename"));
				hTable.setAutoFlush(false, true);
			} catch (IOException e) {
				logger.error("IOException", e);
				return false;
			}
		}
		String colFamily = ConfigReader.getProperty("hbaseColFamily");
		List<Method> methods = Activity.getters();
		for (final Activity act : activityQueue) {
			String rowkey = padding(act.getCompanyId())+padding(act.getProductId())+padding(act.getTimestamp().getTime());
			Put put = new Put(Bytes.toBytes(rowkey));
			for(Method m : methods){
				try {
					Object value = m.invoke(act);
					String v = value==null?null:value.toString();
					if(value!=null){
						put.add(Bytes.toBytes(colFamily), Bytes.toBytes(String.valueOf((m.getName().charAt(3))).toLowerCase()+m.getName().substring(4)), Bytes.toBytes(v));
					}
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
					return false;
				}
			}
			try {
				hTable.put(put);
			} catch (RetriesExhaustedWithDetailsException
					| InterruptedIOException e) {
				e.printStackTrace();
				return false;
			}
		}
		try {
			hTable.flushCommits();
		} catch (RetriesExhaustedWithDetailsException | InterruptedIOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private String placeHolder(int n){
		StringBuilder sql =  new StringBuilder().append("(");
		for(int i=0;i<n-1;i++){
			sql.append("?, ");
		}
		sql.append("?)");
		return sql.toString();
	}
	
	private String updateHolder(List cols){
		List cols_list = Lists.transform( Lists.newArrayList(cols), new Function(){
			@Override
			public Object apply(Object input) {
				return input+"=values("+ input +")";
			}});
		return Joiner.on(",").join(cols_list);
	}
	
	private String padding(Number n){
		String s=null;
		if(n instanceof Long){
			s = Long.toHexString((long)n);
		}else if(n instanceof Integer){
			s = Integer.toHexString((int)n);
		}
		StringBuilder sb =  new StringBuilder();
		for(int i=s.length();i<10;i++){
			sb.append(0);
		}
		return sb.append(s).toString();
	}

}
