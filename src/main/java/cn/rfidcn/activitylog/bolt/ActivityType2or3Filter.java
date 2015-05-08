package cn.rfidcn.activitylog.bolt;

import org.apache.log4j.Logger;

import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;
import cn.rfidcn.activitylog.model.Activity;

public class ActivityType2or3Filter extends  BaseFilter{

	static final Logger logger = Logger.getLogger(ActivityType2or3Filter.class);
	@Override
	public boolean isKeep(TridentTuple tuple) {
//		return true;
		Activity act = (Activity)tuple.get(0);
		return act.getAt()==2 || act.getAt()==3;
	}
	
}
