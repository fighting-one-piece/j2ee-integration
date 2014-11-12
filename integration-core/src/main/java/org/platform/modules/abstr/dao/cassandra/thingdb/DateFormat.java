package org.platform.modules.abstr.dao.cassandra.thingdb;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public enum DateFormat {
	timeFormat("yyyyMMdd HH:mm:ss"), 
	dateFormat("yyyyMMdd"), 
	monthFormat("yyyyMM");

	private String formatString;
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	private DateFormat(String formatString) {
		this.formatString = formatString;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SimpleDateFormat get() {
		ThreadLocal<SimpleDateFormat> threadLocal = (ThreadLocal) 
				sdfMap.get(this.formatString);

		if (null == threadLocal) {
			synchronized (sdfMap) {
				if (null == threadLocal) {
					threadLocal = new ThreadLocal() {
						protected SimpleDateFormat initialValue() {
							return new SimpleDateFormat(
									DateFormat.this.formatString);
						}
					};
					sdfMap.put(this.formatString, threadLocal);
				}
			}
		}
		return (SimpleDateFormat) threadLocal.get();
	}
}
