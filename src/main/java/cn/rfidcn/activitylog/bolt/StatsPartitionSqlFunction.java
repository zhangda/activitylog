package cn.rfidcn.activitylog.bolt;

import java.text.SimpleDateFormat;
import java.util.Date;

import backtype.storm.tuple.Values;
import cn.rfidcn.activitylog.model.Activity;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class StatsPartitionSqlFunction extends BaseFunction{

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		Activity act = (Activity)tuple.get(0);
		
		String company = padding(act.getC());
        String product = padding(act.getPid());
        Date time = act.getTs();
        Date timestamp = new Date(time.getYear(), time.getMonth()+1, time.getDate(), time.getHours(), 0);
		collector.emit(new Values(company,product, timestamp));
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
