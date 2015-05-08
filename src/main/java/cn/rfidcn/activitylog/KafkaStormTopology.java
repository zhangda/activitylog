package cn.rfidcn.activitylog;

import org.apache.log4j.Logger;

import scala.actors.threadpool.Arrays;
import storm.kafka.BrokerHosts;
import storm.kafka.ZkHosts;
import storm.kafka.trident.TransactionalTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.state.StateType;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import cn.rfidcn.activitylog.bolt.ActivityAggregator;
import cn.rfidcn.activitylog.bolt.ActivitySplitFunction;
import cn.rfidcn.activitylog.bolt.ActivityType1Filter;
import cn.rfidcn.activitylog.bolt.ActivityType2or3Filter;
import cn.rfidcn.activitylog.bolt.IndexActivityAggregator;
import cn.rfidcn.activitylog.bolt.ReverseIpFunction;
import cn.rfidcn.activitylog.bolt.StatsPartitionSqlFunction;
import cn.rfidcn.activitylog.scheme.AvroScheme;
import cn.rfidcn.activitylog.state.mysql.MysqlC3p0State;
import cn.rfidcn.activitylog.state.mysql.MysqlStateConfig;
import cn.rfidcn.activitylog.utils.ConfReader;

public class KafkaStormTopology {
	
	static final Logger logger = Logger.getLogger(KafkaStormTopology.class);
	
	public static void main(String args[]) {
		
		BrokerHosts zk = new ZkHosts(ConfReader.zkHosts);
		Config stormConf = new Config(); 
		stormConf.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 1000 * ConfReader.emitTimeInt);
		stormConf.put(Config.TOPOLOGY_WORKERS, ConfReader.num_workers);
		stormConf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS,ConfReader.stormBatchTimeoutSec);
//		stormConf.put(Config.TOPOLOGY_TASKS, 1);
		
        TridentKafkaConfig actSpoutConf = new TridentKafkaConfig(zk, ConfReader.activityTopic);  
        actSpoutConf.fetchSizeBytes =  1024 * 1024 ;
        actSpoutConf.bufferSizeBytes = 1024 * 1024 ;
        actSpoutConf.scheme = new SchemeAsMultiScheme(new AvroScheme());
//        actSpoutConf.forceFromStart  = true;
//      actSpoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        
        TridentTopology topology = new TridentTopology();
        TransactionalTridentKafkaSpout actSpout = new TransactionalTridentKafkaSpout(actSpoutConf);
        
        Stream stream =  topology.newStream("actspoutv2", actSpout).parallelismHint(ConfReader.num_spouts).shuffle()
                .each(new Fields("datalist"), new ActivitySplitFunction(), new Fields("act")).parallelismHint(ConfReader.num_bolts)
//                .each(new Fields("str"), new TestSplitFunction(), new Fields("act")).parallelismHint(Integer.parseInt(confReader.getProperty("num_bolts")))
                .each(new Fields("act"), new ReverseIpFunction(), new Fields("country", "state", "city"));
        
        // persist raw message to database
        stream.partitionAggregate(new Fields("act","country","state", "city"), new ActivityAggregator(), new Fields());
        
        // add index to elasticsearch
        stream.each(new Fields("act"), new ActivityType2or3Filter())
        	.partitionAggregate(new Fields("act"), new IndexActivityAggregator(),  new Fields());
        
        // persist count to mysql
        MysqlStateConfig mysqlStateConfig = new MysqlStateConfig();
		mysqlStateConfig.setUrl(ConfReader.mysqlUrl);
		mysqlStateConfig.setUsername(ConfReader.mysqlUsername);
		mysqlStateConfig.setPassword(ConfReader.mysqlPassword);
		mysqlStateConfig.setTable(ConfReader.mysqlCountTable);
		mysqlStateConfig.setKeyColumns(ConfReader.mysqlCountKeyCol);
		mysqlStateConfig.setValueColumns(ConfReader.mysqlCountValCol);
		mysqlStateConfig.setBatchSize(ConfReader.mysqlBatchSize);
		mysqlStateConfig.setType(StateType.TRANSACTIONAL);
        stream.each(new Fields("act"), new ActivityType1Filter())
                .each(new Fields("act", "country","state", "city"), new StatsPartitionSqlFunction(), new Fields("company","product", "timestamp"))
        		.groupBy(new Fields(Arrays.asList(ConfReader.mysqlCountKeyCol)))
        		.persistentAggregate(MysqlC3p0State.newFactory(mysqlStateConfig), new Count(), new Fields("count"));
       
        try {
			StormSubmitter.submitTopology("activitylog", stormConf, topology.build());
		} catch (AlreadyAliveException e) {
			logger.error("AlreadyAliveException", e);
		} catch (InvalidTopologyException e) {
			logger.error("InvalidTopologyException", e);
		}
        
	}
	

}
