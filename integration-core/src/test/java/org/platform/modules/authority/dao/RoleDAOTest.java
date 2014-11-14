package org.platform.modules.authority.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.auth.dao.IRoleDAO;
import org.platform.modules.auth.entity.Role;
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
public class RoleDAOTest {
	
	private Logger logger = LoggerFactory.getLogger(RoleDAOTest.class);
	
	@Before
	public void before() {
	}

	@Resource(name = "roleHibernateDAO")
	private IRoleDAO roleDAO = null;
	
	@Before
	public void init() {
	}
	
	@Test
	public void testReadRolesByUserId() {
		List<Role> roles = roleDAO.readDataListByUserId(1L);
		System.out.println("roles size: " + roles.size());
		for (Role role : roles) {
			logger.debug("role name: " + role.getName() + " code: " + role.getIdentity());
		}
	}
	
	@Test
	public void testReadRoles() {
		Query condition = new Query();
		QueryResult<Role> qr = roleDAO.readDataPaginationByCondition(condition);
		for (Role role : qr.getResultList()) {
			logger.debug("role name: " + role.getName() + " code: " + role.getIdentity());
		}
	}

}
