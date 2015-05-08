package cn.rfidcn.activitylog.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class ActivityIndices {

	int at;
	int c;
	int pid;
	
	@JSONField (format="yyyy-MM-dd HH:mm:ss")  
	Date ts;
	
	String rk;
	
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public String getRk() {
		return rk;
	}
	public void setRk(String rk) {
		this.rk = rk;
	}
	public int getAt() {
		return at;
	}
	public void setAt(int at) {
		this.at = at;
	}
	
}
