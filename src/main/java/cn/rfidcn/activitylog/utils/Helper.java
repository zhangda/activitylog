package cn.rfidcn.activitylog.utils;

public class Helper {

	public static String padding(Number n){
		String s=null;
		int length = 8;
		if(n instanceof Long){
			s = Long.toHexString((long)n);
			length = 16;
		}else if(n instanceof Integer){
			s = Integer.toHexString((int)n);
		}
		StringBuilder sb =  new StringBuilder();
		for(int i=s.length();i<length;i++){
			sb.append(0);
		}
		return sb.append(s).toString();
	}

}
