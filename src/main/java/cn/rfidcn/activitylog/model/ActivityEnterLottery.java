package cn.rfidcn.activitylog.model;

import java.util.Map;

public class ActivityEnterLottery extends ActivityUserScan{
	Integer pri;
	Integer ptri;
	
	Map<String, Integer> pts;
	Map<String, String> rwds;
	
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
	public Map<String, Integer> getPts() {
		return pts;
	}
	public void setPts(Map<String, Integer> pts) {
		this.pts = pts;
	}
	public Map<String, String> getRwds() {
		return rwds;
	}
	public void setRwds(Map<String, String> rwds) {
		this.rwds = rwds;
	}
	
	
	
	
}
