package org.platform.modules.crawl.controller;

import javax.annotation.Resource;

import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.abstr.controller.GenericController;
import org.platform.modules.crawl.biz.ICrawlBusiness;
import org.platform.modules.crawl.entity.CrawlDetail;
import org.platform.modules.crawl.entity.CrawlJob;
import org.platform.modules.crawl.service.ICrawlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/crawl")
public class CrawlController extends GenericController<CrawlDetail, Long> {

	@Resource(name = "crawlBusiness")
	private ICrawlBusiness crawlBusiness = null;
	
	@Resource(name = "commonCrawlService")
	private ICrawlService commonCrawlService = null;
	
	@Override
	public IGenericBusiness<CrawlDetail, Long> obtainBusinessInstance() {
		return crawlBusiness;
	}
	
	@RequestMapping(value = "/job/startCrawlData", method = RequestMethod.GET)
	public String startCrawlData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				commonCrawlService.start();
			}
		}).start();
		return defaultViewPrefix() + "/job/list";
	}
	
	@RequestMapping(value = "/job/stopCrawlData", method = RequestMethod.GET)
	public String stopCrawlData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				commonCrawlService.stop();
			}
		}).start();
		return defaultViewPrefix() + "/job/list";
	}

	@RequestMapping(value = "/job/index", method = RequestMethod.GET)
	public String index(int type) {
		LOG.debug("start to create index");
		crawlBusiness.insertIndex(type);
		LOG.debug("end to create index");
		return redirectToUrl("/crawl/job/search");
	}
	
	@RequestMapping(value = "/job/search", method = RequestMethod.GET)
	public String search(String keyword, Integer index, Integer currentPageNum,
			Integer rowNumPerPage, Model model) {
		QueryResult<CrawlJob> qr = crawlBusiness.readIndex(keyword, index, currentPageNum, rowNumPerPage);
		if (qr.getTotalRowNum() == 0) {
			Query query = new Query();
			query.setCurrentPageNum(currentPageNum);
			query.setRowNumPerPage(rowNumPerPage);
			query.setPagination(true);
			qr = (QueryResult<CrawlJob>) crawlBusiness.readJobPaginationByCondition(query);
		}
		model.addAttribute("index", index);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPageNum", currentPageNum);
		model.addAttribute("rowNumPerPage", rowNumPerPage);
		model.addAttribute("totalRowNum", qr.getTotalRowNum());
		model.addAttribute("entities", qr.getResultList());
		return defaultViewPrefix() + "/job/list";
	}
	

}