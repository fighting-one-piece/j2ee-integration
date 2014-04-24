package org.platform.modules.crawl.biz.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.platform.utils.json.JSONUtils;
import org.springframework.stereotype.Component;

@Component("tianyaPageBusiness")
public class TianyaPageBusinessImpl extends AbstrPageBusinessImpl{
	
	@Override
	protected Document obtainDocument(String url) throws IOException {
		return null;
	}

	@Override
	protected Map<String, String> handle(Document document) {
		Map<String, String> map = new HashMap<String, String>();
		Elements divElements = document.select("div.title");
		System.out.println("------div start------");
		List<String> titles = new ArrayList<String>();
		for (Element element : divElements) {
			Elements aElements = element.select("a");
			for (Element aElement : aElements) {
				String text = aElement.text();
				System.out.println("a text: " + text);
				if (null != text) {
					titles.add(text);
				}
			}
		}
		System.out.println("titles: " + titles);
		if (titles.size() > 0)
			map.put("title", JSONUtils.object2json(titles.get(0)));
		System.out.println("------div end------");
		return map;
	}

}
