package cn.rfidcn.activitylog;

import java.util.List;

import org.apache.log4j.Logger;

import scala.actors.threadpool.Arrays;
import storm.kafka.BrokerHosts;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.TransactionalTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.state.StateFactory;
import storm.trident.state.StateType;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import cn.rfidcn.activitylog.bolt.ActivityAggregator;
import cn.rfidcn.activitylog.bolt.ActivityTypeStatsFilter;
import cn.rfidcn.activitylog.bolt.ReverseIpFunction;
import cn.rfidcn.activitylog.bolt.StatsPartitionFunction;
import cn.rfidcn.activitylog.bolt.StatsPartitionSqlFunction;
import cn.rfidcn.activitylog.bolt.TestSplitFunction;
import cn.rfidcn.activitylog.state.hbase.HBaseAggregateState;
import cn.rfidcn.activitylog.state.hbase.HbaseTridentConfig;
import cn.rfidcn.activitylog.state.mysql.MysqlState;
import cn.rfidcn.activitylog.state.mysql.MysqlStateConfig;

public class KafkaStormTopology {
	
	static final Logger logger = Logger.getLogger(KafkaStormTopology.class);
	
	public static String zkHosts = "192.168.8.104:2181";
	public static String activityTopic = "activities";
	public static String mysqlUrl = "jdbc:mysql://localhost:3306/pipeline?user=root&password=root";
	public static String mysqlActTable = "activities";
	public static String mysqlCountTable = "activitycounts";
	public static String[] mysqlCountKeyCol = new String[]{"company","product", "timestamp","country","state","city"};
	public static List mysqlCountKeyColList = Arrays.asList(mysqlCountKeyCol);
	public static String[] mysqlCountValCol = new String[]{"count"};
	public static int mysqlBatchSize = 100;
	public static String hbaseActTable= "activities";
	public static String hbaseCountTable = "activitycounts";
	public static String hbaseCountRowkey = "rk" ;
	public static String hbaseColFamily = "log";
	public static String GeoLite2_City = "/root/GeoLite2-City.mmdb";
	
	public static void main(String args[]) {
		
		BrokerHosts zk = new ZkHosts(zkHosts);
		Config stormConf = new Config(); 
		stormConf.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 6000);
		stormConf.put(Config.TOPOLOGY_WORKERS, 1);
//		stormConf.put(Config.TOPOLOGY_TASKS, 1);
		
        TridentKafkaConfig actSpoutConf = new TridentKafkaConfig(zk, activityTopic);  
//      actSpoutConf.scheme = new SchemeAsMultiScheme(new AvroScheme());
        actSpoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        
        TridentTopology topology = new TridentTopology();
        TransactionalTridentKafkaSpout actSpout = new TransactionalTridentKafkaSpout(actSpoutConf);
        
        Stream stream =  topology.newStream("actspout", actSpout).parallelismHint(2).shuffle()
//              .each(new Fields("datalist"), new ActivitySplitFunction(), new Fields("act"))
                .each(new Fields("str"), new TestSplitFunction(), new Fields("act")).parallelismHint(4)
                .each(new Fields("act"), new ReverseIpFunction(), new Fields("country", "state", "city"));
        
        // persist raw message to database
        stream.aggregate(new Fields("act","country","state", "city"), new ActivityAggregator(), new Fields());
        
        // persist count to hbase
//        HbaseTridentConfig hbaseTridentconfig = new HbaseTridentConfig(hbaseCountTable, hbaseCountRowkey);
//        hbaseTridentconfig.setBatch(false);
//        StateFactory hbaseCountState = HBaseAggregateState.transactional(hbaseTridentconfig);
//        stream.each(new Fields("act"), new ActivityTypeStatsFilter())
//        	  .each(new Fields("act", "country","state", "city"), new StatsPartitionFunction(), new Fields("product", "cf","cq"))
//        	  .groupBy(new Fields("product", "cf", "cq"))
//        	  .persistentAggregate(hbaseCountState, new Count(), new Fields("count"));
        
        // persist count to mysql
        MysqlStateConfig mysqlStateConfig = new MysqlStateConfig();
		mysqlStateConfig.setUrl(mysqlUrl);
		mysqlStateConfig.setTable(mysqlCountTable);
		mysqlStateConfig.setKeyColumns(mysqlCountKeyCol);
		mysqlStateConfig.setValueColumns(mysqlCountValCol);
		mysqlStateConfig.setType(StateType.TRANSACTIONAL);
		mysqlStateConfig.setCacheSize(1000);
        stream.each(new Fields("act"), new ActivityTypeStatsFilter())
                .each(new Fields("act", "country","state", "city"), new StatsPartitionSqlFunction(), new Fields("company","product", "timestamp"))
        		.groupBy(new Fields(mysqlCountKeyColList))
        		.persistentAggregate(MysqlState.newFactory(mysqlStateConfig), new Count(), new Fields("count"));
       
        try {
			StormSubmitter.submitTopology("activitylog", stormConf, topology.build());
		} catch (AlreadyAliveException e) {
			logger.error("AlreadyAliveException", e);
		} catch (InvalidTopologyException e) {
			logger.error("InvalidTopologyException", e);
		}
        
	}
	

}
