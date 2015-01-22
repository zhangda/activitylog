package cn.rfidcn.activitylog.bolt;

import java.util.Date;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import cn.rfidcn.activitylog.model.Activity;

public class TestSplitFunction  extends BaseFunction {

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		//ip,companyid,productid
		String s = tuple.getString(0).trim();
		Activity act = new Activity();
		String[] ss = s.split(",");
		act.setOip(ss[0]);
		act.setC(Integer.parseInt(ss[1]));
		act.setTs(new Date());
		act.setPid(Integer.parseInt(ss[2]));
		collector.emit(new Values(act));
	}
	

}
