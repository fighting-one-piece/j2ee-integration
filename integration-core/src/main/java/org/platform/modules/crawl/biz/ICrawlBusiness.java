package org.platform.modules.crawl.biz;

import java.util.List;

import org.platform.entity.QueryCondition;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.modules.crawl.entity.CrawlDetail;
import org.platform.modules.crawl.entity.CrawlJob;
import org.platform.utils.exception.BusinessException;

public interface ICrawlBusiness extends IGenericBusiness<CrawlDetail, Long> {
	
	public void insertIndex(int type) throws BusinessException;

	public void updateStatusF2T(String url, Integer from, Integer to) throws BusinessException;
	
	public CrawlDetail readDataByUrlAndStatus(String url, Integer status) throws BusinessException;
	
	public List<CrawlDetail> readUnCrawlDataList() throws BusinessException;
	
	public QueryResult<CrawlJob> readIndex(QueryCondition condition) throws BusinessException;
	
	public QueryResult<CrawlJob> readJobPaginationByCondition(QueryCondition condition) throws BusinessException;

}

