package cn.rfidcn.trystorm.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

public class ReverseIpClient {
	
	static final Logger logger = Logger.getLogger(ReverseIpClient.class);
	
	static DatabaseReader reader; 
	
	private static void getReader(){
		File database = new File(ConfigReader.getProperty("GeoLite2-City"));
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
	
/*
	public static void main(String[] args) throws IOException, GeoIp2Exception {
		
		// A File object pointing to your GeoIP2 or GeoLite2 database
		File database = new File("c:/GeoLite2-City.mmdb");

		// This creates the DatabaseReader object, which should be reused across
		// lookups.
		DatabaseReader reader = new DatabaseReader.Builder(database).build();

		InetAddress ipAddress = InetAddress.getByName("112.65.18.58");

		// Replace "city" with the appropriate method for your database, e.g.,
		// "country".
		CityResponse response = reader.city(ipAddress);

		Country country = response.getCountry();
		System.out.println(country.getIsoCode());            // 'US'
		System.out.println(country.getName());               // 'United States'
		System.out.println(country.getNames().get("zh-CN")); // '����'

		Subdivision subdivision = response.getMostSpecificSubdivision();
		System.out.println(subdivision.getName());    // 'Minnesota'
		System.out.println(subdivision.getIsoCode()); // 'MN'

		City city = response.getCity();
		System.out.println(city.getName()); // 'Minneapolis'

		Postal postal = response.getPostal();
		System.out.println(postal.getCode()); // '55455'

		Location location = response.getLocation();
		System.out.println(location.getLatitude());  // 44.9733
		System.out.println(location.getLongitude()); // -93.2323
		
	}
*/
	
}
