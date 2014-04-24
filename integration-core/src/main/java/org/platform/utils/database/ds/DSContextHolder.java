package org.platform.utils.database.ds;

public class DSContextHolder {

	 private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	    public static void setDataSourceType(String dataSourceType) {
	      contextHolder.set(dataSourceType);
	    }

	    public static String getDataSourceType() {
	      return contextHolder.get();
	    }

	    public static void clearDataSourceType() {
	      contextHolder.remove();
	    }
}
