package cn.rfidcn.activitylog.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class CommonLogEvent {

    //@JSONField(name = "lv")
    private short logFormatVersion;
    //@JSONField(name = "ts", format = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date timestamp;
   // @JSONField(name = "oip")
    private String originIp;
  //  @JSONField(name = "d")
    private String deploy;
  //  @JSONField(name = "h")
    private String host;
   // @JSONField(name = "v")
    private String version;
   // @JSONField(name = "cl")
    private String clientId;
 //   @JSONField(name = "r")
    private String requestId;
  //  @JSONField(name = "l")
    private String level;
  //  @JSONField(name = "ae")
    private int appEventId;
  //  @JSONField(name = "msg")
    private String msg;
    
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
	
}
