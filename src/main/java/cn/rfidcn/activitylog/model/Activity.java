package cn.rfidcn.activitylog.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Activity implements Serializable {
	
	   short logFormatVersion;
	   
	   Date timestamp;
	  
	   String originIp;
	   
	   String deploy;
	 
	   String host;
	
	   String version;

	   String clientId;
	
	   String requestId;

	    String level;

	   int appEventId;

	   String msg;
	    
	    String honestId;
	    String sequenceNum;
	    
	    int batchId;
	   int productId;
	   int specId;
	   int companyId;
	    
	     String rewardAmount;
	    
	   int promoRuleId;
	    String rewardType;
	    
	    String country;
	    String state;
	    String city;
	    long txid;
	    

	   
		public short getLogFormatVersion() {
			return logFormatVersion;
		}



		public void setLogFormatVersion(short logFormatVersion) {
			this.logFormatVersion = logFormatVersion;
		}



		public Date getTimestamp() {
			return timestamp;
		}



		public void setTimestamp(Date timestamp) {
			this.timestamp = timestamp;
		}



		public String getOriginIp() {
			return originIp;
		}



		public void setOriginIp(String originIp) {
			this.originIp = originIp;
		}



		public String getDeploy() {
			return deploy;
		}



		public void setDeploy(String deploy) {
			this.deploy = deploy;
		}



		public String getHost() {
			return host;
		}



		public void setHost(String host) {
			this.host = host;
		}



		public String getVersion() {
			return version;
		}



		public void setVersion(String version) {
			this.version = version;
		}



		public String getClientId() {
			return clientId;
		}



		public void setClientId(String clientId) {
			this.clientId = clientId;
		}



		public String getRequestId() {
			return requestId;
		}



		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}



		public String getLevel() {
			return level;
		}



		public void setLevel(String level) {
			this.level = level;
		}



		public int getAppEventId() {
			return appEventId;
		}



		public void setAppEventId(int appEventId) {
			this.appEventId = appEventId;
		}



		public String getMsg() {
			return msg;
		}



		public void setMsg(String msg) {
			this.msg = msg;
		}



		public String getHonestId() {
			return honestId;
		}



		public void setHonestId(String honestId) {
			this.honestId = honestId;
		}



		public String getSequenceNum() {
			return sequenceNum;
		}



		public void setSequenceNum(String sequenceNum) {
			this.sequenceNum = sequenceNum;
		}



		public int getBatchId() {
			return batchId;
		}



		public void setBatchId(int batchId) {
			this.batchId = batchId;
		}



		public int getProductId() {
			return productId;
		}



		public void setProductId(int productId) {
			this.productId = productId;
		}



		public int getSpecId() {
			return specId;
		}



		public void setSpecId(int specId) {
			this.specId = specId;
		}



		public String getRewardAmount() {
			return rewardAmount;
		}



		public void setRewardAmount(String rewardAmount) {
			this.rewardAmount = rewardAmount;
		}



		public int getPromoRuleId() {
			return promoRuleId;
		}



		public void setPromoRuleId(int promoRuleId) {
			this.promoRuleId = promoRuleId;
		}



		public String getRewardType() {
			return rewardType;
		}



		public void setRewardType(String rewardType) {
			this.rewardType = rewardType;
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



		public long getTxid() {
			return txid;
		}



		public void setTxid(long txid) {
			this.txid = txid;
		}

		

		public int getCompanyId() {
			return companyId;
		}



		public void setCompanyId(int companyId) {
			this.companyId = companyId;
		}



		public static List<String> getCols() {
			List<String> cols = new ArrayList<String>();
		    Field[] ff = Activity.class.getDeclaredFields();
		    for(Field f: ff){
				cols.add(f.getName());
		    }
		    return cols;
		}
		
		public static List<Method> getters() {
			List<Method> gets = new ArrayList<Method>();
			Field[] ff = Activity.class.getDeclaredFields();
			 for(Field f: ff){
				 String x = f.getName();
				 Method m = null;
				try {
					m = Activity.class.getMethod("get"+(x.charAt(0)+"").toUpperCase()+x.substring(1));
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				 gets.add(m);
			 }
			 return gets;
		}
		
	
		
}
