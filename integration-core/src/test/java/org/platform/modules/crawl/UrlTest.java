package org.platform.modules.crawl;

import java.net.URLEncoder;

public class UrlTest {
	
	public static void main(String[] args) throws Exception {
		System.out.println(URLEncoder.encode("北京", "UTF-8"));
		System.out.println(URLEncoder.encode("北京", "GB2312"));
		System.out.println(URLEncoder.encode("+", "GB2312"));
		System.out.println(URLEncoder.encode("软件工程师", "GB2312"));
		System.out.println(URLEncoder.encode("Java软件工程师", "GB2312"));
		System.out.println(URLEncoder.encode("Java软件工程师", "UTF-8"));
	}
}
