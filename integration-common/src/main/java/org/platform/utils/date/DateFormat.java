package org.platform.utils.date;

import java.util.HashMap;
import java.util.Map;

public enum DateFormat {
	
	TIME("yyyyMMdd HH:mm:ss"), 
	DATE("yyyyMMdd"), 
	MONTH("yyyyMM");

	private String formatStr;
	private static Map<String, ThreadLocal<java.text.SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<java.text.SimpleDateFormat>>();

	private DateFormat(String s) {
		this.formatStr = s;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public java.text.SimpleDateFormat get() {
		ThreadLocal<java.text.SimpleDateFormat> res = (ThreadLocal) 
				sdfMap.get(this.formatStr);

		if (null == res) {
			synchronized (sdfMap) {
				if (null == res) {
					res = new ThreadLocal() {
						protected java.text.SimpleDateFormat initialValue() {
							return new java.text.SimpleDateFormat(
									DateFormat.this.formatStr);
						}
					};
					sdfMap.put(this.formatStr, res);
				}
			}
		}
		return (java.text.SimpleDateFormat) res.get();
	}
}
