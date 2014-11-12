package org.platform.modules.crawl.controller;

import javax.annotation.Resource;

import org.platform.entity.QueryCondition;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.abstr.controller.GenericController;
import org.platform.modules.crawl.biz.ICrawlBusiness;
import org.platform.modules.crawl.entity.CrawlDetail;
import org.platform.modules.crawl.entity.CrawlJob;
import org.platform.modules.crawl.service.ICrawlService;
import org.platform.modules.lucene.IIndex;
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
	public String search(Model model) {
		return search(null, null, null, null, model);
	}

	@RequestMapping(value = "/job/search", method = RequestMethod.POST)
	public String search(Integer currentPageNum, Integer rowNumPerPage, String keyword, Integer index, Model model) {
		QueryCondition condition = new QueryCondition();
		currentPageNum = null == currentPageNum ? 1 : currentPageNum;
		rowNumPerPage = null == rowNumPerPage ? 10 : rowNumPerPage;
		condition.setCurrentPageNum(currentPageNum);
		condition.setRowNumPerPage(rowNumPerPage);
		condition.setPagination(true);
		keyword = null == keyword ? "" : keyword;
		index = null == index ? IIndex.RAM : index;
		condition.addCondition(QueryCondition.LUCENE_KEYWORD, keyword);
		condition.addCondition(QueryCondition.LUCENE_INDEX, index);
		QueryResult<CrawlJob> qr = crawlBusiness.readIndex(condition);
		if (qr.getTotalRowNum() == 0) {
			condition = new QueryCondition();
			condition.setCurrentPageNum(currentPageNum);
			condition.setRowNumPerPage(rowNumPerPage);
			condition.setPagination(true);
			qr = (QueryResult<CrawlJob>) crawlBusiness.readJobPaginationByCondition(condition);
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