package cn.rfidcn.trystorm.bolt;

import org.apache.log4j.Logger;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import cn.rfidcn.trystorm.model.Activity;

public class ActivityConvertFunction extends BaseFunction{

	static final Logger logger = Logger.getLogger(ActivityConvertFunction.class);
	
	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		Activity act = doConvert(tuple.getString(0));
		collector.emit(new Values(act));
	}
	
	private Activity doConvert(String s){
		Activity act = new Activity();
		act.setIp(s);
		return act;
	}
	
	

}
