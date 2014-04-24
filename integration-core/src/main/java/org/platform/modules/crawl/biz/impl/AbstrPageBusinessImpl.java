package org.platform.modules.crawl.biz.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.platform.modules.crawl.biz.ICrawlBusiness;
import org.platform.modules.crawl.biz.IPageBusiness;
import org.platform.modules.crawl.entity.CrawlDetail;
import org.platform.modules.crawl.entity.CrawlDetailExt;
import org.platform.modules.crawl.entity.CrawlDetailStatus;
import org.platform.utils.properties.PropertiesUtils;

public abstract class AbstrPageBusinessImpl implements IPageBusiness {
	
	protected Logger logger = Logger.getLogger(getClass());
	
	@Resource(name = "crawlBusiness")
	protected ICrawlBusiness crawlBusiness = null;
	
	@Override
	public void update(CrawlDetail crawlDetail) {
		Document document = null;
		try {
			document = obtainDocument(crawlDetail.getUrl());
		} catch (IOException e) {
			crawlBusiness.updateStatusF2T(crawlDetail.getUrl(), 
					crawlDetail.getStatus(), CrawlDetailStatus.FAILURE.getValue());
			logger.info(e.getMessage(), e);
		}
		crawlDetail.setStartTime(new Date());
		Map<String, String> map = handle(document);
		crawlDetail.setEndTime(new Date());
		crawlDetail.setStatus(CrawlDetailStatus.SUCCESS.getValue());
		for (Map.Entry<String, String> entry : map.entrySet()) {
			CrawlDetailExt crawlDetailExt = new CrawlDetailExt();
			crawlDetailExt.setKey(entry.getKey());
			crawlDetailExt.setValue(entry.getValue());
			crawlDetailExt.setCrawlDetail(crawlDetail);
			crawlDetail.getCrawlDetailExts().add(crawlDetailExt);
		}
		crawlBusiness.update(crawlDetail);
		postHandle(document, crawlDetail);
	}
	
	protected abstract Document obtainDocument(String url) throws IOException;
	
	protected abstract Map<String, String> handle(Document document);
	
	protected void preHandle(Document document, CrawlDetail crawlDetail) {
		
	}
	
	protected void postHandle(Document document, CrawlDetail parent) {
		int i = 0;
		Elements elements = document.select("div a");
		for (int j = elements.size() / 2, len = elements.size(); j < len; j++) {
			Element element = elements.get(j);
			String value = obtainAttribute(element, "href");
			if (null == value || "".equals(value) || !value.startsWith("http://")) {
				continue;
			}
			value = decorate(value);
			int topN = Integer.parseInt(PropertiesUtils.obtainValue(
					"crawl/crawl.properties", "topN"));
			if ((++i) > topN) break;
			CrawlDetail crawlDetail = new CrawlDetail();
			crawlDetail.setUrl(value);
			crawlDetail.setType(parent.getType());
			crawlDetail.setRule(parent.getRule());
			crawlDetail.setLevel(parent.getLevel() + 1);
			crawlDetail.setTopN(parent.getTopN());
			crawlDetail.setParentUrl(parent.getUrl());
			crawlDetail.setStatus(CrawlDetailStatus.UNCRAWL.getValue());
			crawlBusiness.insert(crawlDetail);
		}
	}
	
	protected String obtainText(Element element) {
		String elementText = element.text();
		System.out.println("text: " + elementText);
		if (null != elementText) {
			elementText = elementText.trim();
		}
		return elementText;
	}
	
	protected String obtainAttribute(Element element, String attributeKey) {
		System.out.println("------element attribute start------");
		String attributeValue = element.attr(attributeKey);
		System.out.println("attributeValue: " + attributeValue);
		if (null != attributeValue) {
			attributeValue = attributeValue.trim();
		}
		System.out.println("------element attribute end------");
		return attributeValue;
	}
	
	protected String decorate(String url) {
		int index = url.lastIndexOf("/");
		int length = url.length();
		return (index == -1 || index != (length - 1)) ? url : url.substring(0, length - 1);
	}


}
