package cn.rfidcn.activitylog.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class ReverseIpClient implements Serializable{
	
	static final Logger logger = Logger.getLogger(ReverseIpClient.class);
	private static DatabaseReader reader; 
	private volatile static ReverseIpClient reverseIpClient;
	
	private ReverseIpClient(){
			File database = new File(ConfReader.GeoLite2_City);
			try {
				reader = new DatabaseReader.Builder(database).build();
			} catch (IOException e) {
				logger.error("IOException", e);
			}
		
	};
	
	public static ReverseIpClient getReverseIpClient(){
		if(reverseIpClient == null){
			synchronized (ReverseIpClient.class) {  
				  if (reverseIpClient == null) {  
					  reverseIpClient = new ReverseIpClient();  
				  }  
			 }
		}
		return reverseIpClient;
	}
	
	public CityResponse getGeoInfo(String ip){
		if(ip!=null && ip.contains(":")){
			ip = ip.substring(0,ip.indexOf(":"));
		}
		InetAddress ipAddress = null;
		try {
			ipAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			//logger.error("UnknownHostException", e);
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
