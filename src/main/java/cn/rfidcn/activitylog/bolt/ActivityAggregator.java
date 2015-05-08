package cn.rfidcn.activitylog.bolt;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import storm.trident.operation.Aggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.topology.TransactionAttempt;
import storm.trident.tuple.TridentTuple;
import backtype.storm.topology.FailedException;
import cn.rfidcn.activitylog.model.Activity;
import cn.rfidcn.activitylog.utils.ConfReader;
import cn.rfidcn.activitylog.utils.Helper;

import com.alibaba.fastjson.JSON;

public class ActivityAggregator implements  Aggregator{

	static final Logger logger = Logger.getLogger(ActivityAggregator.class);
	
	private Queue<Activity> activityQueue;
	static Configuration hbaseConfig;
	static HConnection connection;
	
	@Override
	public void prepare(Map conf, TridentOperationContext context) {
		activityQueue = new LinkedBlockingQueue<Activity>();
		hbaseConfig = HBaseConfiguration.create();
		hbaseConfig.set("hbase.zookeeper.quorum", ConfReader.zkQuorum);
		hbaseConfig.set("hbase.master", ConfReader.hMaster);
		
			try {
//				hTable = new HTable(hbaseConfig,confReader.getProperty("hbaseActTable"));
				if(connection == null){
					connection = HConnectionManager.createConnection(hbaseConfig);
				}
//				hTable.setAutoFlush(false, true);
			} catch (IOException e) {
				logger.error("IOException", e);
			}
	}

	@Override
	public void cleanup() {
		try {
			connection.close();
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
	
		if(persistToHbase()){
			activityQueue.clear();
		}else{
			activityQueue.clear();
			throw new FailedException();
		}	
	}
	
	private boolean persistToHbase(){
		
		HTableInterface hTable;
		try {
			hTable = connection.getTable(ConfReader.hbaseActTable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		String colFamily = ConfReader.hbaseColFamily;
		List<Method> methods = Activity.getters();
		List<Put> puts = new ArrayList<Put>();
		for (final Activity act : activityQueue) {
			String rowkey = Helper.padding(act.getTs().getTime()) + act.getR();
			Put put = new Put(Bytes.toBytes(rowkey), act.getTs().getTime());
			for(Method m : methods){
				try {
					Object value = m.invoke(act);
					String v = value==null?null:value.toString();
					if(value instanceof Map){
						v = value == null?null: JSON.toJSONString(value); 
					}
					if("getTs".equals(m.getName())){
						v = String.valueOf(act.getTs().getTime());
					}
					if(value!=null){
						put.add(Bytes.toBytes(colFamily), Bytes.toBytes(String.valueOf((m.getName().charAt(3))).toLowerCase()+m.getName().substring(4)), Bytes.toBytes(v));
					}
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
					return false;
				}
			}
			puts.add(put);
		}
		try {
			hTable.put(puts);
//			hTable.flushCommits();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			hTable.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
}
