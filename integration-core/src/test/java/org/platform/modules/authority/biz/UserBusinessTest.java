package org.platform.modules.authority.biz;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.auth.biz.IUserBusiness;
import org.platform.modules.auth.dto.UserDTO;
import org.platform.modules.auth.entity.User;
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
public class UserBusinessTest {

	@Resource(name = "userBusiness")
	private IUserBusiness userBusiness = null;
	
	@Before
	public void init() {
	}

	@Test
	public void testInsert() {
		Calendar calendar = Calendar.getInstance();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			UserDTO u = new UserDTO();
			u.setName("test" + i);
			u.setPassword("test" + i);
			u.setCode("test" + i);
			calendar.add(Calendar.DATE, -random.nextInt(20));
			u.setCreateTime(calendar.getTime());
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, random.nextInt(50));
			u.setExpireTime(calendar.getTime());
			u.setAvailan(i % 2 == 0 ? "1" : "0");
			userBusiness.insert(u);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReadDataList() {
		Query condition = new Query();
		QueryResult<User> qr = (QueryResult<User>) userBusiness.readDataListByCondition(condition, false);
		for (User u : qr.getResultList()) {
			System.out.println(u.getName() + ":" + u.getId() + ":" + u.getExpireTime());
		}
	}
	
}
