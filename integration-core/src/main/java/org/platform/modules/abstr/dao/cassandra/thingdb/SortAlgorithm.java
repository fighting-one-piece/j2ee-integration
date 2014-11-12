package org.platform.modules.abstr.dao.cassandra.thingdb;

import java.util.Date;

public class SortAlgorithm {
	
	public static double hot(int ups, int downs, Date date) {
		double score = score(ups, downs);
		double order = Math.log10(Math.max(Math.abs(score), 1));
	    int sign = 0;
		if (score > 0) {
	    	sign = 1;
	    }else if (score < 0) {
	        sign = -1;
	    }
		double seconds = 0;
	    return (sign * order + seconds / 45000);
	}

	public static double score(int ups, int downs) {
	    return ups - downs;
	}
	
	public static double controversy(int ups, int downs) {
		return ups - downs;
	}
	
	public static double confidence(int ups, int downs) {
		return ups - downs;
	}
}
