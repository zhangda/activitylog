package cn.rfidcn.activitylog.model;

import java.util.Date;

public class AppSysLogEvent implements java.io.Serializable{
	   Short lv;
	   Date ts;
	   String oip;
	   String d;
	   String h;
	   String v;
	   String cl;
	   String r;
	   String l;
	   Integer ae;
	   Integer hae;
	   String msg;
	   
	   String geoip;



	public Short getLv() {
		return lv;
	}

	public void setLv(Short lv) {
		this.lv = lv;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getOip() {
		return oip;
	}

	public void setOip(String oip) {
		this.oip = oip;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getCl() {
		return cl;
	}

	public void setCl(String cl) {
		this.cl = cl;
	}

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public Integer getAe() {
		return ae;
	}

	public void setAe(Integer ae) {
		this.ae = ae;
	}

	public Integer getHae() {
		return hae;
	}

	public void setHae(Integer hae) {
		this.hae = hae;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getGeoip() {
		return geoip;
	}

	public void setGeoip(String geoip) {
		this.geoip = geoip;
	}
	   
	
	   
}
