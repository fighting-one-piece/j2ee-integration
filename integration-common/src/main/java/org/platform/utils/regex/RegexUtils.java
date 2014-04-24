package org.platform.utils.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	
	public static String getString(String sourceString, String regexString){
		return getString(sourceString,regexString,0);
	}
	
	public static String getString(String sourceString, String regexString, int targerIndex){
		String result = null;
		Pattern pa = Pattern.compile(regexString);
        Matcher ma = pa.matcher(sourceString);
        while (ma.find()) {
        	result=ma.group(targerIndex);
        }
		return null == result ? "" : result;
	}
	
}
