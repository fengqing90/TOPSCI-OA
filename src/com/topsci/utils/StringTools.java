package com.topsci.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

public final class StringTools {

	public StringTools() {
		// TODO Auto-generated constructor stub
	}
	
	public static String ObjectReplaceToString(Object object){
		if(object==null){
			return "";
		}else{
			return object.toString().trim();
		}
	}
	/**
	 * Replace nullto string.
	 * 
	 * @param javaString the java string
	 * 
	 * @return the string
	 */
	public static String replaceNulltoString(String javaString){
		if(javaString==null){
			return "";
		}else{
			return javaString;
		}
	}
	
	public static String replaceStringtoNull(Object javaString){
		if(javaString==null) return null;
		if(javaString.toString().trim().length()==0) return null;
		if(javaString.toString().trim().equalsIgnoreCase("null")) return null;
		if(javaString.toString().trim().equalsIgnoreCase("Undefined")) return null;
		return javaString.toString().trim();
	}
	
	public static String replaceNulltoString(Object javaString){
		if(javaString==null) return "";
		if(javaString.toString().trim().length()==0) return "";
		if(javaString.toString().trim().equalsIgnoreCase("null")) return "";
		if(javaString.toString().trim().equalsIgnoreCase("Undefined")) return "";
		
		return javaString.toString().trim();
	}
	/**
	 * Gets the bytes.
	 * 
	 * @param source the source
	 * 
	 * @return the bytes
	 * 
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static byte[] getBytes(String source) throws UnsupportedEncodingException{
		if(source == null)
			return null;
		
		if(!source.equals("")){
			source=URLEncoder.encode(source, "UTF-8");
			byte[] converted=source.getBytes("UTF-8");
			return converted;
		}else{
			return null;
		}
	}
	
	/**
	 * Gets the substring by split.
	 * 
	 * @param token the token
	 * @param fullString the full string
	 * 
	 * @return the substring by split
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList getSubstringBySplit(String fullString,String token){
		String temp;
		ArrayList<String> split=new ArrayList<String>();
		StringTokenizer tokenner=new StringTokenizer(fullString,token);
		split.clear();
		while(tokenner.hasMoreElements()){
			temp=tokenner.nextToken();
			split.add(temp);
		}
		return split;
	}
	
	/**
	 * Convert string to int.
	 * 
	 * @param intString the int string
	 * 
	 * @return the int
	 */
	public static int convertStringToInt(String intString){
		int intValue;
		intValue=Integer.valueOf(intString);
		return intValue;
	}
	
	/**
	 * Byte's int type value to string.
	 * 
	 * @param byteIntValue the byte int value
	 * 
	 * @return the string
	 */
	public static String byteIntValueToString(int byteIntValue){
		String convertResult;
		if (byteIntValue >-1 && byteIntValue < 9) {
			convertResult = "" + byteIntValue;
		} else if (byteIntValue > 9 && byteIntValue < 100) {
			convertResult = "a" + byteIntValue;
		} else if (byteIntValue > 99 && byteIntValue < 256) {
			convertResult = "e" + byteIntValue;
		} else {
			convertResult = null;
		}
		return convertResult;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

