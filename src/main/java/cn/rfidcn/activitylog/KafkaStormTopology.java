package cn.rfidcn.activitylog;

import org.apache.log4j.Logger;

import storm.kafka.BrokerHosts;
import storm.kafka.ZkHosts;
import storm.kafka.trident.TransactionalTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.TridentTopology;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import cn.rfidcn.activitylog.bolt.ActivityAggregator;
import cn.rfidcn.activitylog.bolt.ActivitySplitFunction;
import cn.rfidcn.activitylog.bolt.ReverseIpFunction;
import cn.rfidcn.activitylog.scheme.AvroScheme;
import cn.rfidcn.activitylog.utils.ConfigReader;

public class KafkaStormTopology {
	
	static final Logger logger = Logger.getLogger(KafkaStormTopology.class);
	
	public static void main(String args[]) {
		
		BrokerHosts zk = new ZkHosts(ConfigReader.getProperty("zkHosts"));	 
		Config conf = new Config(); 
		conf.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, Integer.parseInt(ConfigReader.getProperty("emitTimeInt")));
		
        TridentKafkaConfig actSpoutConf = new TridentKafkaConfig(zk, ConfigReader.getProperty("activityTopic"));  
        actSpoutConf.scheme = new SchemeAsMultiScheme(new AvroScheme());
//        actSpoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        
        TridentTopology topology = new TridentTopology();
        TransactionalTridentKafkaSpout actSpout = new TransactionalTridentKafkaSpout(actSpoutConf);
        topology.newStream("actspout", actSpout).shuffle()
        .each(new Fields("datalist"), new ActivitySplitFunction(), new Fields("act"))
//        .each(new Fields("str"), new TestSplitFunction(), new Fields("act"))
        .each(new Fields("act"), new ReverseIpFunction(), new Fields("country", "state", "city"))
        .partitionAggregate(new Fields("act","country","state", "city"), new ActivityAggregator(), new Fields());
        
        try {
			StormSubmitter.submitTopology("activitylog", conf, topology.build());
		} catch (AlreadyAliveException e) {
			logger.error("AlreadyAliveException", e);
		} catch (InvalidTopologyException e) {
			logger.error("InvalidTopologyException", e);
		}
        
	}
	

}
