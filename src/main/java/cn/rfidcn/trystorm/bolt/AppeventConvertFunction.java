package cn.rfidcn.trystorm.bolt;

import org.apache.log4j.Logger;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import cn.rfidcn.trystorm.model.Activity;
import cn.rfidcn.trystorm.model.Appevent;

public class AppeventConvertFunction extends BaseFunction{

	static final Logger logger = Logger.getLogger(AppeventConvertFunction.class);
	
	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		Appevent app = doConvert(tuple.getString(0));
		collector.emit(new Values(app));
	}
	
	private Appevent doConvert(String s){
		Appevent app = new Appevent();
		app.setMsg(s);
		return app;
	}
	
	

}
