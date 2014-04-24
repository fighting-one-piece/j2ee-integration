package org.platform.modules.crawl.biz.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.platform.modules.crawl.entity.CrawlDetail;
import org.platform.modules.crawl.entity.CrawlDetailStatus;
import org.platform.modules.crawl.entity.CrawlJob;
import org.platform.utils.http.HttpClientUtils;
import org.platform.utils.http.HttpUtils;
import org.platform.utils.json.JSONUtils;
import org.springframework.stereotype.Component;

@Component("jobPageBusiness")
public class JobPageBusinessImpl extends AbstrPageBusinessImpl {
	
	@Override
	protected Document obtainDocument(String url) throws IOException {
		return Jsoup.parse(HttpClientUtils.get(url, HttpUtils.ENCODE_GB2312));
	}

	@Override
	protected Map<String, String> handle(Document document) {
		Map<String, String> map = new HashMap<String, String>();
		Elements trs = document.select("tr");
		for (Element tr : trs) {
			if(!tr.hasAttr("class")) {
				continue;
			}
			CrawlJob job = new CrawlJob();
			String trClass = tr.attr("class");
			if ("tr0".equals(trClass)) {
				Elements tds = tr.select("td");
				for (Element td : tds) {
					if(!td.hasAttr("class")) {
						continue;
					}
					String tdClass = td.attr("class");
					if ("td1".equals(tdClass)) {
						Elements as = td.select("a");
						for (Element a : as) {
							job.setCareer(a.text());
							break;
						}
					} else if ("td2".equals(tdClass)) {
						Elements as = td.select("a");
						for (Element a : as) {
							job.setCompany(a.text());
							break;
						}
					} else if ("td3".equals(tdClass)) {
						Elements spans = td.select("span");
						for (Element span : spans) {
							job.setLocation(span.text());
							break;
						}
					} else if ("td4".equals(tdClass)) {
						Elements spans = td.select("span");
						for (Element span : spans) {
							job.setUpdateTime(span.text());
							break;
						}
					}
					
				}
			} else if ("tr1".equals(trClass)) {
				Elements tds = tr.select("td.td1234");
				for (Element td : tds) {
					job.setRequire(td.text());
					break;
				}
			} else if ("tr2".equals(trClass)) {
				Elements tds = tr.select("td.td1234");
				for (Element td : tds) {
					job.setSummary(td.text());
					break;
				}
			}
			if (StringUtils.isNotBlank(job.getCareer()) && StringUtils.isNotBlank(job.getCompany())) {
				map.put(job.getCareer() + "-" + job.getCompany(), JSONUtils.object2json(job));
			}
		}
		return map;
	}
	
	@Override
	protected void postHandle(Document document, CrawlDetail parent) {
		Elements as = document.select("tr.tr0 td.td1 a");
		for (Element a : as) {
			System.out.println(a.text() + "-" + a.attr("href"));
			String value = decorate(a.attr("href"));
			CrawlDetail crawlDetail = new CrawlDetail();
			crawlDetail.setUrl(value);
			crawlDetail.setType(parent.getType());
			crawlDetail.setRule("jobDetailPageBusiness");
			crawlDetail.setLevel(parent.getLevel() + 1);
			crawlDetail.setTopN(parent.getTopN());
			crawlDetail.setParentUrl(parent.getUrl());
			crawlDetail.setStatus(CrawlDetailStatus.UNCRAWL.getValue());
			crawlBusiness.insert(crawlDetail);
		}
	}
	
}
