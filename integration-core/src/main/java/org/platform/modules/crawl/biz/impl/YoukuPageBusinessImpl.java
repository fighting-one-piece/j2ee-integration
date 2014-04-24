package org.platform.modules.crawl.biz.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component("youkuPageBusiness")
public class YoukuPageBusinessImpl extends AbstrPageBusinessImpl {
	
	@Override
	protected Document obtainDocument(String url) throws IOException {
		return null;
	}
	
	@Override
	protected Map<String, String> handle(Document document) {
		Map<String, String> map = new HashMap<String, String>();
		Elements divElements = document.select("div.v-meta");
		System.out.println("------div start------");
		for (Element element : divElements) {
			Elements titleElements = element.select("div.v-meta-title a");
			Elements numElements = element.select("div.v-meta-tagrt span.v-num");
			String key = (null != titleElements && titleElements.size() != 0) ? obtainText(titleElements.get(0)) : null;
			String value = (null != numElements && numElements.size() != 0) ? obtainText(numElements.get(0)) : null;
			if (null != key && null != value) {
				map.put(key, value);
			}
		}
		System.out.println("------div end------");
		return map;
	}
	
}
