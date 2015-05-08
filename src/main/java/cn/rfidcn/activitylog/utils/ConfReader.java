package cn.rfidcn.activitylog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfReader implements Serializable{
	
	static final Logger logger = Logger.getLogger(ConfReader.class);
	static private Properties p;
	
	static {
		InputStream in = null;
		in = ClassLoader.getSystemResourceAsStream("config.properties");	
		p = new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
	}
	
	private static String getProperty(String key){
		return p.getProperty(key);
	}
	
	public static final String zkHosts = getProperty("zkHosts");
	public static final int emitTimeInt = Integer.parseInt(getProperty("emitTimeInt"));
	public static final int num_workers = Integer.parseInt(getProperty("num_workers"));
	public static final int stormBatchTimeoutSec = Integer.parseInt(getProperty("stormBatchTimeoutSec"));
	public static final String activityTopic = getProperty("activityTopic");
	public static final int num_spouts = Integer.parseInt(getProperty("num_spouts"));
	public static final int num_bolts = Integer.parseInt(getProperty("num_bolts"));
	public static final String mysqlUrl = getProperty("mysqlUrl");
	public static final String mysqlCountTable = getProperty("mysqlCountTable");
	public static final String mysqlUsername = getProperty("mysqlUsername");
	public static final String mysqlPassword = getProperty("mysqlPassword");
	public static final int mysqlBatchSize = Integer.parseInt(getProperty("mysqlBatchSize"));
	public static final int tridentStateCacheSize = Integer.parseInt(getProperty("tridentStateCacheSize"));
	
	public static final String zkQuorum = getProperty("zkQuorum");
	public static final String hMaster = getProperty("hMaster");
	public static final String hbaseActTable = getProperty("hbaseActTable");
	public static final String hbaseColFamily = getProperty("hbaseColFamily");
	
	public static final String GeoLite2_City = getProperty("GeoLite2-City");
	public static final String[] mysqlCountKeyCol = new String[]{"company","product","timestamp","city","country","state"};
	public static final String[] mysqlCountValCol = new String[]{"count"};
	
	public static final String elasticsearchServer = getProperty("elasticsearchServer");
	public static final String elasticsearchUsername = getProperty("elasticsearchUser");
	public static final String elasticsearchPassword = getProperty("elasticsearchPassword");
}

