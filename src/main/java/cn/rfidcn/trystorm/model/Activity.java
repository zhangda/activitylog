package cn.rfidcn.trystorm.model;

import java.io.Serializable;

public class Activity implements Serializable{
	
	private String ip;
	public String country;
	String state;
	String city;
	
	long txId;
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public long getTxId() {
		return txId;
	}
	public void setTxId(long txId) {
		this.txId = txId;
	}
	
	public static String[] cols = new String[]{"ip", "country", "state", "city", "txid"};
	
}
