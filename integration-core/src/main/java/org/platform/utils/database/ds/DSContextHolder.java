package org.platform.utils.database.ds;

import org.apache.commons.lang3.StringUtils;

public class DSContextHolder {

	public static final String WRITE_DATASOUCE = "master";
	public static final String READ_DATASOUCE = "slave";

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDataSourceType(String dataSourceType) {
		contextHolder.set(dataSourceType);
	}

	public static String getDataSourceType() {
		String dsType = (String) contextHolder.get();
		if (StringUtils.isBlank(dsType)) {
			dsType = WRITE_DATASOUCE;
		}
		return dsType;
	}

	public static void clearDataSourceType() {
		contextHolder.remove();
	}
	
}
