package org.platform.modules.crawl.biz.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.platform.modules.crawl.entity.CrawlJob;
import org.platform.utils.json.JSONUtils;
import org.springframework.stereotype.Component;

@Component("shuimuPageBusiness")
public class ShuimuPageBusinessImpl extends AbstrPageBusinessImpl {
	
	@Override
	protected Document obtainDocument(String url) throws IOException {
		return null;
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
			map.put(job.getCareer() + "-" + job.getCompany(), JSONUtils.object2json(job));
		}
		return map;
	}

}
