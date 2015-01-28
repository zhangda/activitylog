package cn.rfidcn.activitylog.bolt;

import java.util.Map;

import cn.rfidcn.activitylog.model.Activity;
import storm.trident.operation.BaseFilter;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

public class ActivityTypeStatsFilter extends  BaseFilter{

	@Override
	public boolean isKeep(TridentTuple tuple) {
		return true;
//		Activity act = (Activity)tuple.get(0);
//		String actType = act.getActType();
//		return "cn.rfidcn.activitylog.model.ActivityUserScan".equals(actType);
	}

	
}
