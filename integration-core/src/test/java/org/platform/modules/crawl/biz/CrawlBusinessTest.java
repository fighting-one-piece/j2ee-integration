package org.platform.modules.crawl.biz;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.platform.entity.Query;
import org.platform.modules.crawl.entity.CrawlDetail;
import org.platform.modules.crawl.entity.CrawlDetailExt;
import org.platform.modules.crawl.entity.CrawlDetailStatus;
import org.platform.modules.lucene.IIndex;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml",
		"classpath:spring/applicationContext-cache.xml",
		"classpath:spring/applicationContext-redis.xml",
		"classpath:spring/applicationContext-mongo.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class CrawlBusinessTest {
	
	@Resource(name = "crawlBusiness")
	private ICrawlBusiness crawlBusiness = null;
	
	@Before
	public void before() {
	}
	
	@Before
	public void init() {
	}
	
	@Test
	public void testRead() {
		List<CrawlDetail> crawlDetails = crawlBusiness.readUnCrawlDataList();
		for (CrawlDetail crawlDetail : crawlDetails) {
			System.out.println(crawlDetail.getUrl());
		}
	}
	
	@Test
	public void testInsert() {
		CrawlDetail crawlDetail = new CrawlDetail();
		crawlDetail.setUrl("http://www.tianya.com");
		crawlDetail.setStatus(CrawlDetailStatus.UNCRAWL.getValue());
		CrawlDetailExt crawlDetailExt = new CrawlDetailExt();
		crawlDetailExt.setKey("title");
		crawlDetailExt.setValue("tianya");
		crawlDetail.getCrawlDetailExts().add(crawlDetailExt);
		crawlBusiness.insert(crawlDetail);
	}
	
	@Test
	public void testInsertIndex() {
		crawlBusiness.insertIndex(IIndex.FILE);
	}
	
	@Test
	public void testUpdate() {
		Query condition = new Query();
		condition.addHibernateCondition("url", "http://www.tianya.com");
		CrawlDetail crawlDetail = (CrawlDetail) crawlBusiness.readDataByCondition(condition, false);
		CrawlDetailExt crawlDetailExt = new CrawlDetailExt();
		crawlDetailExt.setKey("title1");
		crawlDetailExt.setValue("tianya1");
		crawlDetailExt.setCrawlDetail(crawlDetail);
		crawlDetail.getCrawlDetailExts().add(crawlDetailExt);
		crawlBusiness.update(crawlDetail);
	}
	
	@Test
	public void testReadIndex() {
//		QueryCondition conditions = new QueryCondition();
//		conditions.addCondition(QueryCondition.QUERY, new WildcardQuery(new Term("career", "高级*")));
//		condition.addCondition(QueryCondition.LUCENE_QUERY, new WildcardQuery(new Term("summary", "职位标签*")));
//		QueryResult<CrawlJob> qr = crawlBusiness.readIndex(conditions);
//		for (CrawlJob job : qr.getResultList()) {
//			System.out.println(job.getCareer() + "---" + job.getCompany());
//		}
	}
	
	@Test
	public void testReadIndex1() {
//		QueryCondition conditions = new QueryCondition();
//		conditions.addCondition(QueryCondition.KEYWORD, "职位描述");
//		conditions.addCondition(QueryCondition.INDEX, IIndex.FILE);
//		QueryResult<CrawlJob> qr = crawlBusiness.readIndex(conditions);
//		System.out.println("result number: " + qr.getTotalRowNum());
//		for (CrawlJob job : qr.getResultList()) {
//			System.out.println(job.getSummary());
//			System.out.println(job);
//		}
	}

}
