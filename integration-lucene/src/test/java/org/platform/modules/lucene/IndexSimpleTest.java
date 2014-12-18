package org.platform.modules.lucene;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.lucene.index.Term;
import org.junit.Before;
import org.junit.Test;
import org.platform.entity.User;
import org.platform.modules.lucene.biz.IIndexBusiness;
import org.platform.modules.lucene.biz.impl.FSIndexBusinessImpl;
import org.platform.modules.lucene.biz.impl.RAMIndexBusinessImpl;
import org.platform.modules.lucene.biz.impl.SimpleIndexBusinessImpl;

public class IndexSimpleTest {
	
	private IIndexBusiness fsIndexManager = null;
	
	private IIndexBusiness ramIndexManager = null;
	
	private IIndexBusiness commonIndexManager = null;
	
	private String keyword = "two";
	
	@Before
	public void before() {
		fsIndexManager = new FSIndexBusinessImpl();
		ramIndexManager = new RAMIndexBusinessImpl();
		//fsIndexManager = new FSIndexManager(true);
		commonIndexManager = new SimpleIndexBusinessImpl();
	}

	@Test
	public void testInsertFSIndex() {
		System.out.println("insert fs index start");
		Calendar calendar = Calendar.getInstance();
		Random random = new Random();
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i < 5; i++) {
			User user = new User();
			user.setId(Long.parseLong(String.valueOf(i)));
			user.setName(keyword + i);
			user.setPassword(keyword + i);
			user.setCode(keyword + i);
			calendar.add(Calendar.DATE, -random.nextInt(20));
			user.setCreateTime(calendar.getTime());
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, random.nextInt(50));
			user.setExpireTime(calendar.getTime());
			user.setAvailan(i % 2 == 0 ? "1" : "0");
			userList.add(user);
		}
		fsIndexManager.insert(userList.toArray(new Object[0]));
		//插入数据不提交仍然可以查询出结果
		//fsIndexManager.commit();
		System.out.println("insert fs index end");
	}
	
	@Test
	public void testInertRAMIndex() {
		System.out.println("insert ram index start");
		Calendar calendar = Calendar.getInstance();
		Random random = new Random();
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i < 5; i++) {
			User user = new User();
			user.setId(Long.parseLong(String.valueOf(i)));
			user.setName(keyword + i);
			user.setPassword(keyword + i);
			user.setCode(keyword + i);
			calendar.add(Calendar.DATE, -random.nextInt(20));
			user.setCreateTime(calendar.getTime());
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, random.nextInt(50));
			user.setExpireTime(calendar.getTime());
			user.setAvailan(i % 2 == 0 ? "1" : "0");
			userList.add(user);
		}
		ramIndexManager.insert(userList.toArray(new Object[0]));
		ramIndexManager.commit();
		System.out.println("insert ram index end");
	}
	
	@Test
	public void testDeleteIndex() {
		System.out.println("delete index start");
		for (int i = 0; i < 5; i++) {
			fsIndexManager.delete(new Term("name", keyword + i));
		}
		//删除不提交仍然可以查询出结果
		//fsIndexManager.commit();
		testReadFSByCondition();
		System.out.println("delete index end");
	}
	
	@Test
	public void testReadFSByCondition() {
//		QueryCondition conditions = new QueryCondition();
//		conditions.addCondition(QueryCondition.CURRENT_PAGE_NUM, 0);
//		conditions.addCondition(QueryCondition.ROW_NUM_PER_PAGE, 10);
//		conditions.addCondition(QueryCondition.ENTITY_CLASS, User.class);
//		//condition.addCondition(QueryCondition.LUCENE_KEYWORD, "google");
//		conditions.addCondition(QueryCondition.QUERY, new WildcardQuery(new Term("name", keyword + "*")));
//		Sort sort = new Sort(new SortField("name", Type.STRING));
//		conditions.addCondition(QueryCondition.SORT, sort);
//		QueryResult<User> qr = (QueryResult<User>) fsIndexManager.readDataListByCondition(conditions);
//		System.out.println("fs query total row number: " + qr.getTotalRowNum());
//		for (User u : qr.getResultList()) {
//			System.out.println(u.getId() + ":" + u.getName() + ":" + u.getExpireTime());
//		}
	}
	
	@Test
	public void testReadRAMByCondition() {
//		QueryCondition conditions = new QueryCondition();
//		conditions.addCondition(QueryCondition.CURRENT_PAGE_NUM, 0);
//		conditions.addCondition(QueryCondition.ROW_NUM_PER_PAGE, 10);
//		conditions.addCondition(QueryCondition.ENTITY_CLASS, User.class);
//		//condition.addCondition(QueryCondition.LUCENE_KEYWORD, "google");
//		conditions.addCondition(QueryCondition.QUERY, new WildcardQuery(new Term("name", keyword + "*")));
//		Sort sort = new Sort(new SortField("name", Type.STRING));
//		conditions.addCondition(QueryCondition.SORT, sort);
//		QueryResult<User> qr = (QueryResult<User>) ramIndexManager.readDataListByCondition(conditions);
//		System.out.println("ram query total row number: " + qr.getTotalRowNum());
//		for (User u : qr.getResultList()) {
//			System.out.println(u.getId() + ":" + u.getName() + ":" + u.getExpireTime());
//		}
	}
	
	@Test
	public void testReadCommonByCondition(String keyword) {
//		QueryCondition conditions = new QueryCondition();
//		conditions.addCondition(QueryCondition.CURRENT_PAGE_NUM, 0);
//		conditions.addCondition(QueryCondition.ROW_NUM_PER_PAGE, 10);
//		conditions.addCondition(QueryCondition.ENTITY_CLASS, User.class);
//		//condition.addCondition(QueryCondition.LUCENE_KEYWORD, "google");
//		conditions.addCondition(QueryCondition.QUERY, new WildcardQuery(new Term("name", keyword + "*")));
//		Sort sort = new Sort(new SortField("name", Type.STRING));
//		conditions.addCondition(QueryCondition.SORT, sort);
//		QueryResult<User> qr = (QueryResult<User>) commonIndexManager.readDataListByCondition(conditions);
//		System.out.println("common query total row number: " + qr.getTotalRowNum());
//		for (User u : qr.getResultList()) {
//			System.out.println(u.getId() + ":" + u.getName() + ":" + u.getExpireTime());
//		}
	}
	
	@Test
	public void testReadByCondition() {
		testInertRAMIndex();
		testReadCommonByCondition(keyword);
		commonIndexManager.merge();
		testReadCommonByCondition(keyword);
	}
	
	
}
