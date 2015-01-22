package cn.rfidcn.activitylog.bolt;

import java.text.SimpleDateFormat;
import java.util.Date;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import cn.rfidcn.activitylog.model.Activity;

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
		
		String rowkey = padding(act.getC())+padding(act.getPid());
		
		collector.emit(new Values(rowkey, cfStatsDaily, daily));
		collector.emit(new Values(rowkey, cfStatsMonthly, monthly));
		collector.emit(new Values(rowkey, cfStatsCountry, country));
		collector.emit(new Values(rowkey, cfStatsState, state));
		collector.emit(new Values(rowkey, cfStatsCity, city));
	}
	
	
	private String padding(Number n){
		String s=null;
		if(n instanceof Long){
			s = Long.toHexString((long)n);
		}else if(n instanceof Integer){
			s = Integer.toHexString((int)n);
		}
		StringBuilder sb =  new StringBuilder();
		for(int i=s.length();i<10;i++){
			sb.append(0);
		}
		return sb.append(s).toString();
	}

}
