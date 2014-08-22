package org.platform.modules.crawl.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.platform.entity.QueryCondition;
import org.platform.modules.crawl.entity.CrawlDetailExt;
import org.platform.modules.crawl.entity.CrawlJob;
import org.platform.utils.json.JSONUtils;
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
public class CrawlDAOTest {
	
	@Resource(name = "crawlDetailExtDAO")
	private ICrawlDetailExtDAO crawlDetailExtDAO = null;
	
	@Before
	public void before() {
	}
	
	@Before
	public void init() {
	}
	
	@Test
	public void testReadDataList() {
		QueryCondition condition = new QueryCondition();
		List<CrawlDetailExt> results = crawlDetailExtDAO.readDataListByCondition(condition);
		for (CrawlDetailExt result : results) {
			System.out.println(result.getKey());
		}
		String json = results.get(0).getValue();
		System.out.println(json);
		CrawlJob job = (CrawlJob) JSONUtils.json2Object(json, CrawlJob.class);
		System.out.println(job.getCareer());
	}
	

}
