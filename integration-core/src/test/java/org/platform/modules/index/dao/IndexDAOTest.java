package org.platform.modules.index.dao;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.platform.entity.QueryCondition;
import org.platform.entity.QueryResult;
import org.platform.modules.doc.dao.IIndexDAO;
import org.platform.modules.doc.entity.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml","classpath:spring/applicationContext-attach.xml",
		"classpath:spring/applicationContext-redis.xml","classpath:spring/applicationContext-mongo.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class IndexDAOTest {
	
	private Logger logger = LoggerFactory.getLogger(IndexDAOTest.class);
	
	@Before
	public void before() {
	}

	@Resource(name = "indexHibernateDAO")
	private IIndexDAO indexHibernateDAO = null;
	
	@Before
	public void init() {
	}
	
	@Test
	public void testHibernateInsert() {
		Index index = new Index();
		index.setTitle("title1");
		index.setContent("content1");
		indexHibernateDAO.insert(index);
	}
	
	@Test
	public void testHibernateReadDataListByCondition() {
		QueryCondition condition = new QueryCondition();
		QueryResult<Index> qr = indexHibernateDAO.readDataPaginationByCondition(condition);
		for (Index index : qr.getResultList()) {
			logger.debug("hibernate index pk: " + index.getPK() + " title: " + index.getTitle());
		}
	}
	
}
