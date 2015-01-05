package cn.rfidcn.trystorm;

import org.apache.log4j.Logger;

import storm.kafka.BrokerHosts;
import storm.kafka.StringScheme;
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
import cn.rfidcn.trystorm.bolt.ActivityAggregator;
import cn.rfidcn.trystorm.bolt.ActivityConvertFunction;
import cn.rfidcn.trystorm.bolt.AppeventAggregator;
import cn.rfidcn.trystorm.bolt.AppeventConvertFunction;
import cn.rfidcn.trystorm.bolt.ReverseIpFuntion;
import cn.rfidcn.trystorm.utils.ConfigReader;

public class KafkaStormTopology {
	
	static final Logger logger = Logger.getLogger(KafkaStormTopology.class);
	
	public static void main(String args[]) {
		
		BrokerHosts zk = new ZkHosts(ConfigReader.getProperty("zkHosts"));	 
		Config conf = new Config(); 
		conf.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, Integer.parseInt(ConfigReader.getProperty("emitTimeInt")));
        TridentKafkaConfig actSpoutConf = new TridentKafkaConfig(zk, ConfigReader.getProperty("activityTopic"));
        actSpoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
		
        TridentTopology topology = new TridentTopology();
        TransactionalTridentKafkaSpout actSpout = new TransactionalTridentKafkaSpout(actSpoutConf);
        topology.newStream("actspout", actSpout).shuffle()
        .each(new Fields("str"),new ActivityConvertFunction(), new Fields("actObj"))
        .each(new Fields("actObj"), new ReverseIpFuntion(), new Fields("actWgeo"))
        .partitionAggregate(new Fields("actWgeo"), new ActivityAggregator(), new Fields(""));
        
        TridentKafkaConfig appSpoutConf = new TridentKafkaConfig(zk, ConfigReader.getProperty("appeventTopic"));
        appSpoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        TransactionalTridentKafkaSpout appSpout = new TransactionalTridentKafkaSpout(appSpoutConf);
        topology.newStream("appspout", appSpout).shuffle()
        .each(new Fields("str") , new AppeventConvertFunction(), new Fields("appObj"))
        .partitionAggregate(new Fields("appObj"), new AppeventAggregator(), new Fields(""));
        
        try {
			StormSubmitter.submitTopology("eventlog", conf, topology.build());
			
		} catch (AlreadyAliveException e) {
			logger.error("AlreadyAliveException", e);
		} catch (InvalidTopologyException e) {
			logger.error("InvalidTopologyException", e);
		}
        
	}
	

}
