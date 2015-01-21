package cn.rfidcn.activitylog;

import org.apache.log4j.Logger;

import storm.kafka.BrokerHosts;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.TransactionalTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.state.StateType;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import cn.rfidcn.activitylog.bolt.ReverseIpFunction;
import cn.rfidcn.activitylog.bolt.StatsPartitionSqlFunction;
import cn.rfidcn.activitylog.bolt.TestSplitFunction;
import cn.rfidcn.activitylog.state.mysql.MysqlState;
import cn.rfidcn.activitylog.state.mysql.MysqlStateConfig;
import cn.rfidcn.activitylog.utils.ConfigReader;

public class KafkaStormTopology {
	
	static final Logger logger = Logger.getLogger(KafkaStormTopology.class);
	
	public static void main(String args[]) {
		
		BrokerHosts zk = new ZkHosts(ConfigReader.getProperty("zkHosts"));	 
		Config conf = new Config(); 
		conf.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, Integer.parseInt(ConfigReader.getProperty("emitTimeInt")));
		
        TridentKafkaConfig actSpoutConf = new TridentKafkaConfig(zk, ConfigReader.getProperty("activityTopic"));  
//        actSpoutConf.scheme = new SchemeAsMultiScheme(new AvroScheme());
        actSpoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        
        TridentTopology topology = new TridentTopology();
        TransactionalTridentKafkaSpout actSpout = new TransactionalTridentKafkaSpout(actSpoutConf);
        
//        TridentConfig config = new TridentConfig("testcount", "product");
//        config.setBatch(false);
//        StateFactory state = HBaseAggregateState.transactional(config);
        
        final MysqlStateConfig config = new MysqlStateConfig();
		{
			config.setUrl("jdbc:mysql://localhost:3306/test?user=root&password=root");
			config.setTable("trystorm");
			config.setKeyColumns(new String[]{"content","country","state","city"});
			config.setValueColumns(new String[]{"count"});
			config.setType(StateType.TRANSACTIONAL);
			config.setCacheSize(1000);
		}
		
        topology.newStream("actspout", actSpout).shuffle()
//        .each(new Fields("datalist"), new ActivitySplitFunction(), new Fields("act"))
        .each(new Fields("str"), new TestSplitFunction(), new Fields("act"))
        .each(new Fields("act"), new ReverseIpFunction(), new Fields("country", "state", "city"))
//        .partitionAggregate(new Fields("act","country","state", "city"), new ActivityAggregator(), new Fields());
        
//        .each(new Fields("act", "country","state", "city"), new StatsPartitionFunction(), new Fields("product", "cf","cq")).project(new Fields("product", "cf", "cq"))
//        .groupBy(new Fields("product", "cf", "cq")).persistentAggregate(state, new Count(), new Fields("count"));
         .each(new Fields("act", "country","state", "city"), new StatsPartitionSqlFunction(), new Fields("content"))
         //.project(new Fields("content", "country","state","city")).groupBy(new Fields("content", "country","state","city"))
        .persistentAggregate(MysqlState.newFactory(config), new Fields("content", "country","state", "city"), new Count(), new Fields("count"));
        
        
        try {
			StormSubmitter.submitTopology("activitylog", conf, topology.build());
		} catch (AlreadyAliveException e) {
			logger.error("AlreadyAliveException", e);
		} catch (InvalidTopologyException e) {
			logger.error("InvalidTopologyException", e);
		}
        
	}
	

}
