package org.platform.modules.crawl.service;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.platform.modules.crawl.service.ICrawlService;
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
public class CrawlServiceTest {
	
	@Resource(name = "jobCrawlService")
	private ICrawlService jobCrawlService = null;
	
	@Resource(name = "commonCrawlService")
	private ICrawlService commonCrawlService = null;
	
	@Before
	public void before() {
	}
	
	@Before
	public void init() {
	}
	
	@Test
	public void startJobCrawl() {
		jobCrawlService.start();
	}
	
	@Test
	public void startCommonCrawl() {
		commonCrawlService.start();
	}
	
	@Test
	public void stopCommonCrawl() {
		commonCrawlService.stop();
	}

}
