package org.platform.modules.authority.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.platform.modules.auth.dao.IResourceDAO;
import org.platform.modules.auth.entity.Resource;
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
public class ResourceDAOTest {

	private Logger logger = LoggerFactory.getLogger(RoleDAOTest.class);
	
	@javax.annotation.Resource(name = "resourceHibernateDAO")
	private IResourceDAO resourceDAO = null;
	
	@Before
	public void before() {
	}
	
	@Test
	public void testReadDataByPk() {
		Resource resource = resourceDAO.readDataByPK(1L);
		logger.debug("resource name: " + resource.getName());
		logger.debug("resource children: " + resource.getChildren().size());
		for (Resource child : resource.getChildren()) {
			logger.debug("child name: " + child.getName());
		}
	}
	
	@Test
	public void testReadDataParentByPk() {
		Resource resource = resourceDAO.readDataByPK(2L);
		logger.debug("resource name: " + resource.getName());
		logger.debug("resource parent id: " + resource.getParent().getId());
		logger.debug("resource parent name : " + resource.getParent().getName());
	}
}
