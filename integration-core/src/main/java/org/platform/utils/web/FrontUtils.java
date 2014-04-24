package org.platform.utils.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.platform.utils.web.Constants;
import org.platform.utils.web.request.RequestUtils;

public class FrontUtils {
	
	/**
	 * 为前台模板设置公用数据
	 * @param request
	 * @param model
	 */
	public static void frontData(HttpServletRequest request, Map<String, Object> map) {
		String location = RequestUtils.getLocation(request);
		Long startTime = (Long) request.getAttribute(Constants.START_TIME);
		frontData(map, location, startTime);
	}

	public static void frontData(Map<String, Object> map, String location, Long startTime) {
		if (startTime != null) {
			map.put(Constants.START_TIME, startTime);
		}
		
	}

}
