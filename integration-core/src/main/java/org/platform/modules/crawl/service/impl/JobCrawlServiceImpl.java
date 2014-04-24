package org.platform.modules.crawl.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.platform.modules.crawl.biz.ICrawlBusiness;
import org.platform.modules.crawl.biz.IPageBusiness;
import org.platform.modules.crawl.entity.CrawlDetail;
import org.platform.modules.crawl.entity.CrawlDetailStatus;
import org.platform.utils.exception.BusinessException;
import org.platform.utils.spring.SpringUtils;
import org.springframework.stereotype.Service;

@Service("jobCrawlService")
public class JobCrawlServiceImpl extends AbstrCrawlServiceImpl {
	
	@Resource(name = "crawlBusiness")
	private ICrawlBusiness crawlBusiness = null;

	protected void initData(Properties properties) {
		try {
			String initialUrl = properties.getProperty("initial.url");
			String keyword = URLEncoder.encode(properties.getProperty("initial.keyword"), "UTF-8");
			String keywordLocation = properties.getProperty("initial.keyword.location");
			String pageLocation = properties.getProperty("initial.page.location");
			int offset = Integer.parseInt(properties.getProperty("initial.page.offset"));
			int limit = Integer.parseInt(properties.getProperty("initial.page.limit"));
			
			for (int i = offset; i <= limit; i++) {
				String url = initialUrl;
				url = url.replace(keywordLocation, keyword);
				url = url.replace(pageLocation, String.valueOf(i));
				CrawlDetail crawlDetail = new CrawlDetail();
				crawlDetail.setUrl(url);
				crawlDetail.setLevel(1);
				crawlDetail.setRule("jobPageBusiness");
				crawlDetail.setStatus(CrawlDetailStatus.UNCRAWL.getValue());
				crawlBusiness.insert(crawlDetail);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "hiding" })
	@Override
	public <String> void doProduce(BlockingQueue<String> queue) {
		try {
			String url = (String) readUnCrawlUrl();
			int i = 0;
			while (null == url && i < 5) {
				waitTime(10);
				url = (String) readUnCrawlUrl();
				i++;
			}
			logger.info("put url: " + url);
			while (null != url) {
				queue.put(url);
				url = (String) readUnCrawlUrl();
			}
		} catch (InterruptedException | BusinessException e) {
			logger.debug(e.getMessage(), e);
		}
	}
	
	@Override
	public void doConsume(BlockingQueue<?> queue) {
		try {
			String url = (String) queue.take();
			logger.info("take url: " + url);
			while (null != url && !"".equals(url)) {
				try {
					CrawlDetail crawlDetail = crawlBusiness.readDataByUrlAndStatus(
							url, CrawlDetailStatus.CRAWLING.getValue());
					if (null == crawlDetail.getRule()) {
						crawlBusiness.updateStatusF2T(url, CrawlDetailStatus.CRAWLING.getValue(),
								CrawlDetailStatus.FAILURE.getValue());
						throw new BusinessException("没有对应规则,无法处理");
					}
					IPageBusiness page = SpringUtils.getBean(crawlDetail.getRule());
					page.update(crawlDetail);
				} catch (BusinessException be) {
					crawlBusiness.updateStatusF2T(url, CrawlDetailStatus.CRAWLING.getValue(),
							CrawlDetailStatus.FAILURE.getValue());
					logger.info(be.getMessage(), be);
				}
				url = (String) queue.take();
			}
		} catch (InterruptedException | BusinessException e) {
			logger.debug(e.getMessage(), e);
		}
	}
	
	private static List<String> urls = new ArrayList<String>();
	
	private synchronized String readUnCrawlUrl() {
		String url = null;
		try {
			if (urls.size() == 0) {
				List<CrawlDetail> crawlDetails = crawlBusiness.readUnCrawlDataList();
				if (null == crawlDetails || crawlDetails.size() == 0) {
					waitTime(10);
					crawlDetails = crawlBusiness.readUnCrawlDataList();
				}
				for (CrawlDetail crawlDetail : crawlDetails) {
					urls.add(crawlDetail.getUrl());
				}
			}
			url = urls.size() > 0 ? urls.remove(0) : null;
			if (null != url) {
				crawlBusiness.updateStatusF2T(url, CrawlDetailStatus.UNCRAWL.getValue(),
						CrawlDetailStatus.CRAWLING.getValue());
			}
		} catch (BusinessException be) {
			logger.info(be.getMessage(), be);
		}
		return url;
	}

}
