package org.platform.modules.authority.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.auth.dao.IUserDAO;
import org.platform.modules.auth.dao.impl.hibernate.UserDAOImpl;
import org.platform.modules.auth.entity.User;
import org.platform.utils.message.MessageUtils;
import org.platform.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class UserDAOTest {
	
	private Logger logger = LoggerFactory.getLogger(UserDAOTest.class);
	
	@Before
	public void before() {
	}

	@Resource(name = "userHibernateDAO")
	private IUserDAO userHibernateDAO = null;
	
	@Resource(name = "userMyBatisDAO")
	private IUserDAO userMyBatisDAO = null;
	
	@Resource(name = "userRedisDAO")
	private IUserDAO userRedisDAO = null;
	
	@Resource(name = "userMongoDAO")
	private IUserDAO userMongoDAO = null;
	
	@Before
	public void init() {
	}
	
	@Test
	public void testHibernateInsert() {
		User user = new User();
		user.setName("zhangsan10");
		user.setPassword("zhangsan10");
		user.setIdentity("zhangsan10");
		user.setCreateTime(new Date());
		user.setExpireTime(new Date());
		user.setAvailan(Boolean.TRUE);
		userHibernateDAO.insert(user);
	}
	
	@Test
	public void testMyBatisInsert() {
		User user = new User();
		user.setName("zhangsan");
		user.setPassword("zhangsan");
		user.setIdentity("zhangsan");
		user.setCreateTime(new Date());
		user.setExpireTime(new Date());
		user.setAvailan(Boolean.TRUE);
		userMyBatisDAO.insert(user);
	}
	
	@Test
	public void testRedisInsert() {
		User user = new User();
		user.setName("zhangsan");
		user.setPassword("zhangsan");
		user.setIdentity("zhangsan");
		user.setCreateTime(new Date());
		user.setExpireTime(new Date());
		user.setAvailan(Boolean.TRUE);
		userRedisDAO.insert(user);
	}
	
	@Test
	public void testMongoInsert() {
		for (int i = 5; i < 10; i++) {
			User user = new User();
			user.setName("zhangsan" + i);
			user.setPassword("zhangsan" + i);
			user.setIdentity("zhangsan" + i);
			user.setCreateTime(new Date());
			user.setExpireTime(new Date());
			user.setAvailan(Boolean.TRUE);
			userMongoDAO.insert(user);
		}
	}
	
	@Test
	public void testRedisReadDataByPK() {
		User user = userRedisDAO.readDataByPK(1L);
		System.out.println(user.getName());
	}
	
	@Test
	public void testMongoReadDataByPK() {
		User user = userMongoDAO.readDataByPK(1L);
		System.out.println(user.getName());
	}
	
	@Test
	public void testMongoReadDataListByCondition() {
		QueryResult<User> qr = userMongoDAO.readDataPaginationByCondition(null);
		for (User user : qr.getResultList()) {
			System.out.println(user.getName());
		}
	}
	
	@Test
	public void testMyBatisReadDataListByCondition() {
		Query condition = new Query();
		QueryResult<User> qr = userMyBatisDAO.readDataPaginationByCondition(condition);
		for (User user : qr.getResultList()) {
			logger.debug("mybatis user name: " + user.getName());
		}
	}
	
	@Test
	public void testHibernateReadDataListByCondition() {
		Query condition = new Query();
		QueryResult<User> qr = userHibernateDAO.readDataPaginationByCondition(condition);
		for (User user : qr.getResultList()) {
			logger.debug("hibernate user id: " + user.getId() + " name: " + user.getName());
		}
	}
	
	@Test
	public void testSpringUtils() {
		IUserDAO userDAO = SpringUtils.getBean(UserDAOImpl.class);
		Query condition = new Query();
		QueryResult<User> qr = userDAO.readDataPaginationByCondition(condition);
		for (User user : qr.getResultList()) {
			logger.debug("hibernate user name: " + user.getName());
		}
	}
	
	@Test
	public void testMessageUtils() {
		String message = MessageUtils.getMessage("jcaptcha.validate.success", null, null);
		System.out.println("message: " + message);
	}

}
