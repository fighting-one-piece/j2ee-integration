package org.platform.modules.abstr.common;

import java.util.Calendar;
import java.util.Date;


public class SortAlgorithm {
	
	private static Date epochTime = null;
	
	static {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1970);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		epochTime = calendar.getTime();
	}
	
	public static long epochSeconds(Date date) {
		return date.getTime() - epochTime.getTime();
	}
	
	public static double hot(int ups, int downs, Date date) {
		double score = score(ups, downs);
		double order = Math.log10(Math.max(Math.abs(score), 1));
	    int sign = score > 0 ? 1 : score < 0 ? -1 : 0;
		double seconds = epochSeconds(date) - 1134028003;
	    return Math.round((order + sign * seconds / 45000));
	}

	public static double score(int ups, int downs) {
	    return ups - downs;
	}
	
	public static double rising(int ups, int downs) {
		if (ups == 0) return 0;
	    return ups / Math.max(ups + downs, 1);
	}
	
	public static double controversial(int ups, int downs) {
		if (ups <= 0 || downs <= 0) return 0;
		double n = 0;
		if (ups > downs) {
			n =  Double.parseDouble(String.valueOf(downs)) / ups;
		} else {
			n =  Double.parseDouble(String.valueOf(ups)) / downs;
		}
		return Math.pow((ups + downs), n);
	}
	
	public static double confidence(int ups, int downs) {
		return ups - downs;
	}
}
