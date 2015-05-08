package cn.rfidcn.activitylog.bolt;

import static org.elasticsearch.shield.authc.support.UsernamePasswordToken.basicAuthHeaderValue;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.shield.authc.support.SecuredString;

import storm.trident.operation.Aggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.topology.TransactionAttempt;
import storm.trident.tuple.TridentTuple;
import backtype.storm.topology.FailedException;
import cn.rfidcn.activitylog.model.Activity;
import cn.rfidcn.activitylog.model.ActivityIndices;
import cn.rfidcn.activitylog.utils.ConfReader;
import cn.rfidcn.activitylog.utils.Helper;

import com.alibaba.fastjson.JSON;

public class IndexActivityAggregator implements  Aggregator{

	static final Logger logger = Logger.getLogger(IndexActivityAggregator.class);
	
	private Queue<Activity> activityQueue;
	Client esClient;
	String token;
	
	@Override
	public void prepare(Map conf, TridentOperationContext context) {
		activityQueue = new LinkedBlockingQueue<Activity>();
		//esClient = new TransportClient().addTransportAddress(new InetSocketTransportAddress(ConfReader.elasticsearchServer, 9300));
		esClient = new TransportClient(ImmutableSettings.builder()
			    .put("cluster.name", "elasticsearch")
			    .put("shield.user", ConfReader.elasticsearchUsername+":"+ConfReader.elasticsearchPassword).build())
		.addTransportAddress(new InetSocketTransportAddress(ConfReader.elasticsearchServer, 9300));
		token = basicAuthHeaderValue(ConfReader.elasticsearchUsername, new SecuredString(ConfReader.elasticsearchPassword.toCharArray()));
	}

	@Override
	public void cleanup() {
		esClient.close();
	}

	@Override
	public Object init(Object batchId, TridentCollector collector) {
		return ((TransactionAttempt) batchId).getTransactionId();
	}

	@Override
	public void aggregate(Object val, TridentTuple tuple, TridentCollector collector) {
		Activity act = (Activity) tuple.get(0);
		activityQueue.add(act);
	}

	@Override
	public void complete(Object val, TridentCollector collector) {
		if(activityQueue.size()==0) 
			return;
		logger.info("index activity to elasticsearch, # of records: " + activityQueue.size());
	
		if(persistToElasticsearch()){
			activityQueue.clear();
		}else{
			activityQueue.clear();
			throw new FailedException();
		}	
	}
	
	private boolean persistToElasticsearch(){
		BulkRequestBuilder bb = esClient.prepareBulk().putHeader("Authorization", token);
		for (final Activity act : activityQueue) {
			ActivityIndices ai = new ActivityIndices();
			ai.setAt(act.getAt());
			ai.setC(act.getC());
			ai.setPid(act.getPid());
			ai.setTs(act.getTs());
			ai.setRk(Helper.padding(act.getTs().getTime()) + act.getR());
			bb.add(esClient.prepareIndex("activityindex", "activity", ai.getRk()).setSource(JSON.toJSONString(ai)));
		}
		
		try{
			BulkResponse response = bb.execute().get();
			return !response.hasFailures();
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
}
