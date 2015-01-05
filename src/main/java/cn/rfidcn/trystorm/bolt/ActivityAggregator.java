package cn.rfidcn.trystorm.bolt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import storm.trident.operation.Aggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.topology.TransactionAttempt;
import storm.trident.tuple.TridentTuple;
import backtype.storm.topology.FailedException;
import cn.rfidcn.trystorm.model.Activity;
import cn.rfidcn.trystorm.utils.ConfigReader;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class ActivityAggregator implements  Aggregator{

	static final Logger logger = Logger.getLogger(ActivityAggregator.class);
	
	private Connection connection;
	private Queue<Activity> activityQueue = new ConcurrentLinkedQueue<Activity>();
	
	@Override
	public void prepare(Map conf, TridentOperationContext context) {
		
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				logger.error("ClassNotFoundException", e);
			}
			try {
				connection = DriverManager.getConnection(ConfigReader.getProperty("mysqlConn"));
			} catch (SQLException e) {
				logger.error("SQLException", e);
			}
		
	}

	@Override
	public void cleanup() {
	}

	@Override
	public Object init(Object batchId, TridentCollector collector) {
		return ((TransactionAttempt) batchId).getTransactionId();
	}

	@Override
	public void aggregate(Object val, TridentTuple tuple, TridentCollector collector) {
		Activity act = (Activity) tuple.get(0);
		act.setTxId((long) val);
		activityQueue.add(act);
	}

	@Override
	public void complete(Object val, TridentCollector collector) {
		if(activityQueue.size()==0) 
			return;
		logger.info("persist to database, # of records: " + activityQueue.size());
		try {
			if(connection == null || connection.isClosed())
				connection = DriverManager.getConnection(ConfigReader.getProperty("mysqlConn"));
			} catch(SQLException e){
				logger.error("SQLException", e);
		}
		
		//String sql = "INSERT INTO trystorm (ip, country, state, city, txid) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE ip=values(ip), country=values(country), state=values(state), city=values(city), txid=values(txid)";
		String sql = new StringBuilder().append("INSERT INTO ")
				.append(ConfigReader.getProperty("activityTable"))
				.append(" (")
				.append(Joiner.on(",").join(Activity.cols))
				.append(")")
				.append(" VALUES ")
				.append(placeHolder(Activity.cols.length))
				.append(" ON DUPLICATE KEY UPDATE ")
				.append(updateHolder(Activity.cols))
				.toString();
		PreparedStatement ps =null;
		try {
			int i=0;
			int batchSize = Integer.parseInt(ConfigReader.getProperty("mysqlBatchSize"));
			ps = connection.prepareStatement(sql);
			for (final Activity act : activityQueue) {
				ps.setString(1, act.getIp());
				ps.setString(2, act.getCountry());
				ps.setString(3, act.getState());
				ps.setString(4, act.getCity());
				ps.setLong(5, act.getTxId());
				ps.addBatch();
				if(++i %batchSize == 0)
					ps.executeBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			logger.error("SQLException", e);
			throw new FailedException();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					logger.error("SQLException", ex);
				}
			}
			activityQueue.clear();
		}
	}
	
	private String placeHolder(int n){
		StringBuilder sql =  new StringBuilder().append("(");
		for(int i=0;i<n-1;i++){
			sql.append("?, ");
		}
		sql.append("?)");
		return sql.toString();
	}
	
	private String updateHolder( String[] cols){
		List cols_list = Lists.transform( Lists.newArrayList(cols), new Function(){
			@Override
			public Object apply(Object input) {
				return input+"=values("+ input +")";
			}});
		return Joiner.on(",").join(cols_list);
	}
	

	
	
	

}
