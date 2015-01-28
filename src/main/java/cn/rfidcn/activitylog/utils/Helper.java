package cn.rfidcn.activitylog.utils;

public class Helper {

	public static String padding(Number n){
		String s=null;
		if(n instanceof Long){
			s = Long.toHexString((long)n);
		}else if(n instanceof Integer){
			s = Integer.toHexString((int)n);
		}
		StringBuilder sb =  new StringBuilder();
		for(int i=s.length();i<8;i++){
			sb.append(0);
		}
		return sb.append(s).toString();
	}
}
