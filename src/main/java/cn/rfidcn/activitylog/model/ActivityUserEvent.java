package cn.rfidcn.activitylog.model;


// Activity Log Types defined here:
// http://redmine.toucha.org/projects/zhabei/wiki/Log_Format
// This class will also capture all third-party login IDs
public abstract class ActivityUserEvent extends CommonLogEvent {

    private short activityType = -1;
    private String phoneNumber;
    private String userId;
    private String taobaoId;
    private String wechatId;
    
	public short getActivityType() {
		return activityType;
	}
	public void setActivityType(short activityType) {
		this.activityType = activityType;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTaobaoId() {
		return taobaoId;
	}
	public void setTaobaoId(String taobaoId) {
		this.taobaoId = taobaoId;
	}
	public String getWechatId() {
		return wechatId;
	}
	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

  
}