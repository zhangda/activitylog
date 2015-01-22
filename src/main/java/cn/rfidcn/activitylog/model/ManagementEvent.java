package cn.rfidcn.activitylog.model;

public class ManagementEvent extends AppSysLogEvent {
	  int c;
	  String u;
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


}
