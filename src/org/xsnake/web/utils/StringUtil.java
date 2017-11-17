package org.xsnake.web.utils;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


public class StringUtil extends StringUtils{

	public static Gson gson = new Gson();
	
	public static String getUUID(){
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid.toUpperCase();
	}
	
	public static boolean arraySearch(String[] array, String str){
		if(array == null){
			return false;
		}
		if(str == null){
			return false;
		}
		for(String s : array){
			if(str.equals(s)){
				return true;
			}
		}
		return false;
	}
	
	public static String getString(String str){
		try {
			return new String(str.getBytes("UTF-8"),"ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String toJson(Object obj){
		return gson.toJson(obj);
	}

	public static String getSearchString(Object object) {

		if (object == null) {
			return null;
		}
		if (object instanceof String) {
			return (String) object;
		}
		throw new RuntimeException("请注意条件类型");
	}
	
	public static boolean isNullOrEmpty(Object obj){
		if(obj == null || "".equals(obj)){
			return true;
		}
		return false;
	}
	
	public static String strnull(Object obj){
		if(obj == null || "".equals(obj)){
			return "";
		}
		return String.valueOf(obj);
	}
	
	public static int nullToZeroInt(Object obj){
		if(obj == null || "".equals(obj)){
			return 0;
		}
		return Integer.parseInt(strnull(obj));
	}
	
	public static Long nullToZeroLong(Object obj){
		if(obj == null || "".equals(obj)){
			return 0L;
		}
		return Long.parseLong(strnull(obj));
	}
}
