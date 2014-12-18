package org.platform.modules.crawl.biz.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.platform.entity.Query;
import org.platform.entity.QueryItem;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.biz.impl.GenericBusinessImpl;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.modules.crawl.biz.ICrawlBusiness;
import org.platform.modules.crawl.dao.ICrawlDetailDAO;
import org.platform.modules.crawl.dao.ICrawlDetailExtDAO;
import org.platform.modules.crawl.entity.CrawlDetail;
import org.platform.modules.crawl.entity.CrawlDetailExt;
import org.platform.modules.crawl.entity.CrawlDetailStatus;
import org.platform.modules.crawl.entity.CrawlJob;
import org.platform.modules.lucene.IIndex;
import org.platform.modules.lucene.biz.IIndexBusiness;
import org.platform.modules.lucene.biz.impl.FSIndexBusinessImpl;
import org.platform.modules.lucene.biz.impl.RAMIndexBusinessImpl;
import org.platform.modules.lucene.entity.QueryCondition;
import org.platform.utils.exception.BusinessException;
import org.platform.utils.json.JSONUtils;
import org.springframework.stereotype.Service;

@Service("crawlBusiness")
public class CrawlBusinessImpl extends GenericBusinessImpl<CrawlDetail, Long> implements ICrawlBusiness {
	
	@Resource(name = "crawlDetailDAO")
	private ICrawlDetailDAO crawlDetailDAO = null;
	
	@Resource(name = "crawlDetailExtDAO")
	private ICrawlDetailExtDAO crawlDetailExtDAO = null;
	
	@Override
	protected IGenericDAO<CrawlDetail, Long> obtainDAOInstance() {
		return crawlDetailDAO;
	}
	
	@Override
	protected void preHandle(Object object) throws BusinessException {
		CrawlDetail crawlDetail = (CrawlDetail) object;
		if (null == crawlDetail.getId()) {
			Query condition = new Query();
			condition.addHibernateCondition("url", crawlDetail.getUrl());
			condition.addHibernateCondition("status", crawlDetail.getStatus());
			CrawlDetail dbCrawlDetail = crawlDetailDAO.readDataByCondition(condition);
			if (null != dbCrawlDetail) {
				throw new BusinessException("记录已经存在");
			}
		}
	}
	
	@Override
	public void insertIndex(int type) throws BusinessException {
		Query condition = new Query();
		List<CrawlDetailExt> crawlDetailExts = crawlDetailExtDAO.readDataListByCondition(condition);
		List<CrawlJob> resultList = new ArrayList<CrawlJob>();
		for (CrawlDetailExt crawlDetailExt : crawlDetailExts) {
			CrawlJob crawlJob = (CrawlJob) JSONUtils.json2Object(
					crawlDetailExt.getValue(), CrawlJob.class);
			LOG.debug(crawlJob);
			resultList.add(crawlJob);
		}
		IIndexBusiness indexManager = type == IIndex.RAM ? new RAMIndexBusinessImpl() : new FSIndexBusinessImpl();
		indexManager.insert(resultList.toArray(new Object[0]));
		indexManager.commit();
	}
	
	@Override
	public void updateStatusF2T(String url, Integer from, Integer to) throws BusinessException {
		CrawlDetail crawlDetail = readDataByUrlAndStatus(url, from);
		crawlDetail.setStatus(to);
		crawlDetailDAO.update(crawlDetail);
	}
	
	@Override
	public CrawlDetail readDataByUrlAndStatus(String url, Integer status) throws BusinessException {
		Query condition = new Query();
		condition.addHibernateCondition("url", url);
		condition.addHibernateCondition("status", status);
		List<CrawlDetail> crawlDetails = crawlDetailDAO.readDataListByCondition(condition);
		if (crawlDetails.size() != 1) {
			throw new BusinessException("根据URL和STATUS获取数据错误");
		}
		return crawlDetails.get(0);
	}
	
	@Override
	public List<CrawlDetail> readUnCrawlDataList() throws BusinessException {
		Query condition = new Query();
		List<QueryItem> qis = new ArrayList<QueryItem>();
		qis.add(new QueryItem("status", CrawlDetailStatus.UNCRAWL.getValue()));
		qis.add(new QueryItem("status", CrawlDetailStatus.FAILURE.getValue()));
		condition.addHibernateCondition("status", qis, QueryItem.MATCH_OR);
		return crawlDetailDAO.readDataListByCondition(condition);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public QueryResult<CrawlJob> readIndex(String keyword, Integer index, Integer currentPageNum,
			Integer rowNumPerPage) throws BusinessException {
		QueryCondition condition = new QueryCondition();
		condition.setEntityClass(CrawlJob.class);
		condition.setKeyword(keyword);
		condition.setQueryTag("c");
		String[] queryFields = new String[]{"career", "company", "location", 
				"updateTime", "require", "summary"};
		condition.setQueryFields(queryFields);
		String[] highLighterFields = new String[]{"career", "company", "require", "summary"};
		condition.setHighLighterFields(highLighterFields);
		condition.setCurrentPageNum(currentPageNum);
		condition.setRowNumPerPage(rowNumPerPage);
		IIndexBusiness indexManager = index == IIndex.RAM ? 
				new RAMIndexBusinessImpl() : new FSIndexBusinessImpl();
		return (QueryResult<CrawlJob>) indexManager.readDataListByCondition(condition);
	}
	
	@Override
	public QueryResult<CrawlJob> readJobPaginationByCondition(Query query) throws BusinessException {
		QueryResult<CrawlDetailExt> qr = crawlDetailExtDAO.readDataPaginationByCondition(query);
		List<CrawlJob> resultList = new ArrayList<CrawlJob>();
		for (CrawlDetailExt crawlDetailExt : qr.getResultList()) {
			resultList.add((CrawlJob) JSONUtils.json2Object(
					crawlDetailExt.getValue(), CrawlJob.class));
		}
		QueryResult<CrawlJob> jobQR = new QueryResult<CrawlJob>();
		jobQR.setTotalRowNum(qr.getTotalRowNum());
		jobQR.setResultList(resultList);
		return jobQR;
	}
	
}
