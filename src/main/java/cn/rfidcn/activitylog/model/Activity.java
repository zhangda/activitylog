package cn.rfidcn.activitylog.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Activity implements Serializable {

	   Integer c;
	   String u;
	   
	   Short lv;
	   Date ts;
	   String oip;
	   String d;
	   String h;
	   String v;
	   String cl;
	   String r;
	   String l;
	   String ae;
	   Integer hae;
	   String msg;
	   
	   String bid;
	   Integer pid;
	   String sid;
	   String hid;
	   String num;
	   Short at;
	   String pn;
	   String tu;
	   String wu;

	   // enter lottery
	Integer pri;
	Integer ptri;
	Map<CharSequence, Integer> pts;
	Map<CharSequence, String> rwds;
	  
	// claim reward
	Integer rid;
	String rn;
	String rt;
	String ra;
	Integer okid;
	String ok;
	String vid;
	String bkn;
	String bkhn;
	String bkhi;
	String bkc;
	
	   String country;
	   String state;
	   String city;
	   Long txid;
	    
	String geoip;
	   
	
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
	}
	public Integer getOkid() {
		return okid;
	}
	public void setOkid(Integer okid) {
		this.okid = okid;
	}
	public Short getLv() {
		return lv;
	}
	public void setLv(Short lv) {
		this.lv = lv;
	}
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
	public Integer getHae() {
		return hae;
	}
	public void setHae(Integer hae) {
		this.hae = hae;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Long getTxid() {
		return txid;
	}
	public void setTxid(Long txid) {
		this.txid = txid;
	}
	public String getGeoip() {
		return geoip;
	}
	public void setGeoip(String geoip) {
		this.geoip = geoip;
	}

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
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

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
	
	
	public Integer getPri() {
		return pri;
	}
	public void setPri(Integer pri) {
		this.pri = pri;
	}
	public Integer getPtri() {
		return ptri;
	}
	public void setPtri(Integer ptri) {
		this.ptri = ptri;
	}
	public Map<CharSequence, Integer> getPts() {
		return pts;
	}
	public void setPts(Map<CharSequence, Integer> pts) {
		this.pts = pts;
	}
	public Map<CharSequence, String> getRwds() {
		return rwds;
	}
	public void setRwds(Map<CharSequence, String> rwds) {
		this.rwds = rwds;
	}
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public String getRt() {
		return rt;
	}
	public void setRt(String rt) {
		this.rt = rt;
	}
	public String getRa() {
		return ra;
	}
	public void setRa(String ra) {
		this.ra = ra;
	}
	public String getOk() {
		return ok;
	}
	public void setOk(String ok) {
		this.ok = ok;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getBkn() {
		return bkn;
	}
	public void setBkn(String bkn) {
		this.bkn = bkn;
	}
	public String getBkhn() {
		return bkhn;
	}
	public void setBkhn(String bkhn) {
		this.bkhn = bkhn;
	}
	public String getBkhi() {
		return bkhi;
	}
	public void setBkhi(String bkhi) {
		this.bkhi = bkhi;
	}
	public String getBkc() {
		return bkc;
	}
	public void setBkc(String bkc) {
		this.bkc = bkc;
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

