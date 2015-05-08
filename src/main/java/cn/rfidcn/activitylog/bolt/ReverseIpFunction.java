package cn.rfidcn.activitylog.bolt;

import java.util.Map;

import org.apache.log4j.Logger;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;
import cn.rfidcn.activitylog.model.Activity;
import cn.rfidcn.activitylog.utils.ReverseIpClient;

import com.maxmind.geoip2.model.CityResponse;

public class ReverseIpFunction extends BaseFunction{
	static final Logger logger = Logger.getLogger(ReverseIpFunction.class);

	private ReverseIpClient reverseIpClient;
	
	@Override
	public void prepare(Map conf, TridentOperationContext context) {
		 reverseIpClient = ReverseIpClient.getReverseIpClient();
	}

	public void execute(TridentTuple tuple, TridentCollector collector) {
		
		Activity act = (Activity)tuple.get(0);
		CityResponse response = null;
		response = reverseIpClient.getGeoInfo(act.getOip());
		String country = "";
		String state = "";
		String city = "";
		if(response!=null){
			country = response.getCountry().getNames().get("zh-CN");
			if("中国".equals(country)){
				state =  response.getMostSpecificSubdivision().getNames().get("zh-CN");
				city = response.getCity().getNames().get("zh-CN");
			}else{
				country = response.getCountry().getName();
				state =  response.getMostSpecificSubdivision().getName();
				city = response.getCity().getName();
			}
		}
		country = country==null?"":country;
		state = state==null?"":state;
		city = city==null?"":city;
		collector.emit(new Values(country, state, city));
	}
}
