package cn.rfidcn.activitylog.bolt;

import java.text.SimpleDateFormat;
import java.util.Date;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import cn.rfidcn.activitylog.model.Activity;
import cn.rfidcn.activitylog.utils.Helper;

public class StatsPartitionFunction extends BaseFunction {

	final String rk = "rk";
	final String cfStatsDaily = "daily";
	final String cfStatsMonthly = "monthly";
	final String cfStatsCountry = "country";
	final String cfStatsState = "state";
	final String cfStatsCity = "city";
	

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		
		Activity act = (Activity)tuple.get(0);
		Date date = act.getTs();
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		String daily = format.format(date);
		String monthly = daily.substring(0,6);
		String country = daily + tuple.getString(1);
		String state = daily + tuple.getString(2);
		String city = daily + tuple.getString(3);
		
		String rowkey = Helper.padding(act.getC())+Helper.padding(act.getPid());
		
		collector.emit(new Values(rowkey, cfStatsDaily, daily));
		collector.emit(new Values(rowkey, cfStatsMonthly, monthly));
		collector.emit(new Values(rowkey, cfStatsCountry, country));
		collector.emit(new Values(rowkey, cfStatsState, state));
		collector.emit(new Values(rowkey, cfStatsCity, city));
	}

}
