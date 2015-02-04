package cn.rfidcn.activitylog.bolt;

import java.util.Date;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import cn.rfidcn.activitylog.model.Activity;
import cn.rfidcn.activitylog.utils.Helper;

public class StatsPartitionSqlFunction extends BaseFunction{

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		Activity act = (Activity)tuple.get(0);
		
		String company = Helper.padding(act.getC());
        String product = Helper.padding(act.getPid());
        Date time = act.getTs();
        Date timestamp = new Date(time.getYear(), time.getMonth()+1, time.getDate(), time.getHours(), 0);
		collector.emit(new Values(company,product, timestamp));
	}

	
	

	
}
