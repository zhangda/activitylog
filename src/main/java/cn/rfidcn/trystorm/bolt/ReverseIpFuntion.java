package cn.rfidcn.trystorm.bolt;

import org.apache.log4j.Logger;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import cn.rfidcn.trystorm.model.Activity;
import cn.rfidcn.trystorm.utils.ReverseIpClient;

import com.maxmind.geoip2.model.CityResponse;

public class ReverseIpFuntion extends BaseFunction{
	static final Logger logger = Logger.getLogger(ReverseIpFuntion.class);

	public void execute(TridentTuple tuple, TridentCollector collector) {
		
		Activity act = (Activity)tuple.get(0);
		CityResponse response = null;
		response = ReverseIpClient.getGeoInfo(act.getIp());
		if(response!=null){
			act.setCountry(response.getCountry().getNames().get("zh-CN"));
			act.setState( response.getMostSpecificSubdivision().getNames().get("zh-CN"));
			act.setCity(response.getCity().getNames().get("zh-CN"));
		}
		collector.emit(new Values(act));
	}
}
