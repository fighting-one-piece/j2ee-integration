package org.platform.test;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.platform.utils.properties.PropertiesUtils;


public class Test {

	public static void main(String[] args) throws Exception {
		Map<Integer, List<Integer>> intMap = new HashMap<Integer, List<Integer>>();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(2);list1.add(1);
		intMap.put(1, list1);
		print(intMap);
		System.out.println(URLEncoder.encode("Java软件工程师", "UTF-8"));
		System.out.println(URLEncoder.encode("Java软件工程师", "GB2312"));
		
		Properties properties = PropertiesUtils.newInstance("crawl/crawl.properties");
		for (Entry<Object, Object> entry : properties.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}
	
	public static <T> void print(Map<T, List<T>> map) {
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
		}
	}
}
