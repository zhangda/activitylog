package cn.rfidcn.activitylog.model;

public class ActivityUserScan extends AppSysLogEvent{

	Integer c;
	String bid;
    Integer pid;
	String sid;
	String hid;
	String num;
	Short at;
	String pn;
	String u;
	String tu;
	String wu;
	

	public Short getAt() {
		return at;
	}
	public void setAt(Short at) {
		this.at = at;
	}
	public Integer getC() {
		return c;
	}
	public void setC(Integer c) {
		this.c = c;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getU() {
		return u;
	}
	public void setU(String u) {
		this.u = u;
	}
	public String getTu() {
		return tu;
	}
	public void setTu(String tu) {
		this.tu = tu;
	}
	public String getWu() {
		return wu;
	}
	public void setWu(String wu) {
		this.wu = wu;
	}
	
	
}
