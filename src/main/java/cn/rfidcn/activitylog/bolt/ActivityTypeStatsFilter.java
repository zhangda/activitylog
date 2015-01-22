package cn.rfidcn.activitylog.bolt;

import cn.rfidcn.activitylog.model.Activity;
import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

public class ActivityTypeStatsFilter extends  BaseFilter{

	@Override
	public boolean isKeep(TridentTuple tuple) {
		Activity act = (Activity)tuple.get(0);
		String actType = act.getActType();
		return !"cn.rfidcn.activitylog.model.ManagementEvent".equals(actType);
	}

}
