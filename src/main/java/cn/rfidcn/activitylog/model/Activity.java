package cn.rfidcn.activitylog.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Activity implements Serializable {

	   int c;
	   String u;
	   
	   short lv;
	   Date ts;
	   String oip;
	   String d;
	   String h;
	   String v;
	   String cl;
	   String r;
	   String l;
	   String ae;
	   int hae;
	   String msg;
	   
	   int bid;
	   int pid;
	   int sid;
	   String hid;
	   String num;
	   short at;
	   String pn;
	   String tu;
	   String wu;

	   
	   int pri;
	   String prt;
	   String pra;
	  
	   String country;
	   String state;
	   String city;
	   long txid;
	   String actType;
	    
	   
	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public short getLv() {
		return lv;
	}

	public void setLv(short lv) {
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

	public String getAe() {
		return ae;
	}

	public void setAe(String ae) {
		this.ae = ae;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

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

	public int getHae() {
		return hae;
	}

	public void setHae(int hae) {
		this.hae = hae;
	}

	public int getPri() {
		return pri;
	}

	public void setPri(int pri) {
		this.pri = pri;
	}

	public String getPrt() {
		return prt;
	}

	public void setPrt(String prt) {
		this.prt = prt;
	}

	public String getPra() {
		return pra;
	}

	public void setPra(String pra) {
		this.pra = pra;
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

