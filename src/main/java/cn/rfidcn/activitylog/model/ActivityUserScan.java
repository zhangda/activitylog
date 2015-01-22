package cn.rfidcn.activitylog.model;

public class ActivityUserScan extends AppSysLogEvent{

	int bid;
	int pid;
	int sid;
	String hid;
	String num;
	short at;
	String pn;
	String u;
	String tu;
	String wu;
	
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
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
