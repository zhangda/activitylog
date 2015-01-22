package cn.rfidcn.activitylog.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import cn.rfidcn.activitylog.KafkaStormTopology;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class ReverseIpClient {
	
	static final Logger logger = Logger.getLogger(ReverseIpClient.class);
	
	static DatabaseReader reader; 
	
	private static void getReader(){
		File database = new File(KafkaStormTopology.GeoLite2_City);
		try {
			reader = new DatabaseReader.Builder(database).build();
		} catch (IOException e) {
			logger.error("IOException", e);
		}
	}

	public static CityResponse getGeoInfo(String ip){
		if(reader == null) getReader();
		InetAddress ipAddress = null;
		try {
			ipAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			logger.error("UnknownHostException", e);
			return null;
		}
		try {
			return reader.city(ipAddress);
		} catch (IOException e) {
			logger.error("IOException", e);
		} catch (GeoIp2Exception e) {
			logger.error("GeoIp2Exception", e);
		}
		return null;
	}
	
}
