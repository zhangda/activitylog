package cn.rfidcn.activitylog.bolt;

import org.apache.log4j.Logger;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import cn.rfidcn.activitylog.model.Activity;
import cn.rfidcn.activitylog.utils.ReverseIpClient;

import com.maxmind.geoip2.model.CityResponse;

public class ReverseIpFunction extends BaseFunction{
	static final Logger logger = Logger.getLogger(ReverseIpFunction.class);

	public void execute(TridentTuple tuple, TridentCollector collector) {
		
		Activity act = (Activity)tuple.get(0);
		CityResponse response = null;
		response = ReverseIpClient.getGeoInfo(act.getOriginIp());
		String country = "";
		String state = "";
		String city = "";
		if(response!=null){
			country = response.getCountry().getName();
			state =  response.getMostSpecificSubdivision().getName();
			city = response.getCity().getName();
		}
		collector.emit(new Values(country, state, city));
	}
}
