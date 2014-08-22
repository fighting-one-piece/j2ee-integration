package org.platform.modules.doc.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.platform.entity.QueryCondition;
import org.platform.entity.QueryResult;
import org.platform.modules.doc.entity.Doc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class DocDAOTest {
	
	private Logger logger = LoggerFactory.getLogger(DocDAOTest.class);
	
	@Before
	public void before() {
	}
	
	@Resource(name = "docMyBatisDAO")
	private IDocDAO docMyBatisDAO = null;
	
	@Before
	public void init() {
	}
	
	@Test
	public void testMyBatisReadDataByPK() {
		Doc doc = docMyBatisDAO.readDataByPK(10101L);
		logger.debug("mybatis doc title: " + doc.getTitle());
	}
	
	@Test
	public void testMyBatisReadDataListByCondition() {
		QueryCondition condition = new QueryCondition();
		condition.addCondition("id", 10101L);
		List<Doc> docs = docMyBatisDAO.readDataListByCondition(condition);
		for (Doc doc : docs) {
			logger.debug("mybatis doc title: " + doc.getTitle());
		}
	}
	
	@Test
	public void testMyBatisReadDataPaginationByCondition() {
		QueryCondition condition = new QueryCondition();
		condition.setPagination(true);
		condition.addMybatisCondition(QueryCondition.OFFSET, 0);
		condition.addMybatisCondition(QueryCondition.LIMIT, 50);
		QueryResult<Doc> qr = docMyBatisDAO.readDataPaginationByCondition(condition);
		for (Doc doc : qr.getResultList()) {
			logger.debug("mybatis doc title: " + doc.getTitle());
		}
	}
	
}
