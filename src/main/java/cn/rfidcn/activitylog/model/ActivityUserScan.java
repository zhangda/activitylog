package cn.rfidcn.activitylog.model;

public class ActivityUserScan extends AppSysLogEvent{

	String bid;
    String pid;
	String sid;
	String hid;
	String num;
	short at;
	String pn;
	String u;
	String tu;
	String wu;
	

	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
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
	public short getAt() {
		return at;
	}
	public void setAt(short at) {
		this.at = at;
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
