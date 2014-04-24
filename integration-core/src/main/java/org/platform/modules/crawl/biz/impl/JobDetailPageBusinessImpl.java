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
import org.platform.modules.crawl.entity.CrawlJob;
import org.platform.utils.http.HttpClientUtils;
import org.platform.utils.http.HttpUtils;
import org.platform.utils.json.JSONUtils;
import org.springframework.stereotype.Component;

@Component("jobDetailPageBusiness")
public class JobDetailPageBusinessImpl extends AbstrPageBusinessImpl {
	
	private String url = null;
	
	@Override
	protected Document obtainDocument(String url) throws IOException {
		this.url = url;
		return Jsoup.parse(HttpClientUtils.get(url, HttpUtils.ENCODE_GB2312));
	}

	@Override
	protected Map<String, String> handle(Document document) {
		Map<String, String> map = new HashMap<String, String>();
		CrawlJob job = new CrawlJob();
		job.setLink(url);
		String career = document.select("table.jobs_1 tr td.sr_bt").get(0).text();
		job.setCareer(career);
		String company = document.select("table.jobs_1 tr table a").get(0).text();
		job.setCompany(company);
		StringBuffer require = new StringBuffer();
		StringBuffer summary = new StringBuffer();
		Elements tds = document.select("div.grayline table.jobs_1 tr td");
		for (Element td : tds) {
			if (!td.hasAttr("class"))
				continue;
			String tdClass = td.attr("class");
			if ("txt_1".equals(tdClass) || "txt_2".equals(tdClass)) {
				require.append(td.text()).append(" ");
			} else if ("job_detail".equals(tdClass) || "txt_4 wordBreakNormal job_detail ".equals(tdClass)) {
				summary.append(td.text());
			}
		}
		job.setRequire(require.toString());
		job.setSummary(summary.toString());
		if (StringUtils.isNotBlank(job.getCareer()) && StringUtils.isNotBlank(job.getCompany())) {
			map.put(job.getCareer() + "-" + job.getCompany(), JSONUtils.object2json(job));
		}
		return map;
	}
	
	@Override
	protected void postHandle(Document document, CrawlDetail crawlDetail) {
	}
	

}
