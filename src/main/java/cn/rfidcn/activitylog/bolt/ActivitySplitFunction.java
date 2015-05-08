package cn.rfidcn.activitylog.bolt;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import cn.rfidcn.activitylog.model.Activity;

public class ActivitySplitFunction  extends BaseFunction{

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		  List<Object> acts = (List)tuple.get(0);
		  for(Object act: acts){
			  Activity flatAct = new Activity();
			  try {
				BeanUtils.copyProperties(flatAct, act);
				if(flatAct.getBid()==null){
					flatAct.setBid("");
				}
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			  collector.emit(new Values(flatAct));
		  }
	}

}
