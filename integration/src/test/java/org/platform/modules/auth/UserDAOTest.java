package org.platform.modules.auth;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.platform.modules.auth.dao.IUserDAO;
import org.platform.modules.auth.entity.User;
import org.platform.modules.auth.mapper.UserMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@Transactional
public class UserDAOTest {
	
	@Resource(name = "userDAO")
	private IUserDAO userDAO = null;

	@Resource(name = "userMapper")
	private UserMapper userMapper = null;
	
	@Test
	public void testReadDataById() {
		User user = userDAO.readDataByPK(1L);
		System.out.println("user name: " + user.getName());
	}
	
	@Test
	public void testMapperReadDataById() {
		User user = userMapper.readDataByPK(1L);
		System.out.println("user name: " + user.getName());
	}
	
}
