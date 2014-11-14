package org.platform.modules.lucene;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.WildcardQuery;
import org.junit.Before;
import org.junit.Test;
import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.entity.User;
import org.platform.modules.lucene.FSIndexManager;
import org.platform.modules.lucene.IIndexManager;
import org.platform.modules.lucene.RAMIndexManager;
import org.platform.modules.lucene.SimpleIndexManager;

public class IndexSimpleTest {
	
	private IIndexManager fsIndexManager = null;
	
	private IIndexManager ramIndexManager = null;
	
	private IIndexManager commonIndexManager = null;
	
	private String keyword = "two";
	
	@Before
	public void before() {
		fsIndexManager = new FSIndexManager();
		ramIndexManager = new RAMIndexManager();
		//fsIndexManager = new FSIndexManager(true);
		commonIndexManager = new SimpleIndexManager();
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReadFSByCondition() {
		Query condition = new Query();
		condition.addCondition(Query.CURRENT_PAGE_NUM, 0);
		condition.addCondition(Query.ROW_NUM_PER_PAGE, 10);
		condition.addCondition(Query.LUCENE_CLASS, User.class);
		//condition.addCondition(QueryCondition.LUCENE_KEYWORD, "google");
		condition.addCondition(Query.LUCENE_QUERY, new WildcardQuery(new Term("name", keyword + "*")));
		Sort sort = new Sort(new SortField("name", Type.STRING));
		condition.addCondition(Query.LUCENE_SORT, sort);
		QueryResult<User> qr = (QueryResult<User>) fsIndexManager.readByCondition(condition);
		System.out.println("fs query total row number: " + qr.getTotalRowNum());
		for (User u : qr.getResultList()) {
			System.out.println(u.getId() + ":" + u.getName() + ":" + u.getExpireTime());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReadRAMByCondition() {
		Query condition = new Query();
		condition.addCondition(Query.CURRENT_PAGE_NUM, 0);
		condition.addCondition(Query.ROW_NUM_PER_PAGE, 10);
		condition.addCondition(Query.LUCENE_CLASS, User.class);
		//condition.addCondition(QueryCondition.LUCENE_KEYWORD, "google");
		condition.addCondition(Query.LUCENE_QUERY, new WildcardQuery(new Term("name", keyword + "*")));
		Sort sort = new Sort(new SortField("name", Type.STRING));
		condition.addHibernateCondition(Query.LUCENE_SORT, sort);
		QueryResult<User> qr = (QueryResult<User>) ramIndexManager.readByCondition(condition);
		System.out.println("ram query total row number: " + qr.getTotalRowNum());
		for (User u : qr.getResultList()) {
			System.out.println(u.getId() + ":" + u.getName() + ":" + u.getExpireTime());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReadCommonByCondition(String keyword) {
		Query condition = new Query();
		condition.addCondition(Query.CURRENT_PAGE_NUM, 0);
		condition.addCondition(Query.ROW_NUM_PER_PAGE, 10);
		condition.addCondition(Query.LUCENE_CLASS, User.class);
		//condition.addCondition(QueryCondition.LUCENE_KEYWORD, "google");
		condition.addCondition(Query.LUCENE_QUERY, new WildcardQuery(new Term("name", keyword + "*")));
		Sort sort = new Sort(new SortField("name", Type.STRING));
		condition.addCondition(Query.LUCENE_SORT, sort);
		QueryResult<User> qr = (QueryResult<User>) commonIndexManager.readByCondition(condition);
		System.out.println("common query total row number: " + qr.getTotalRowNum());
		for (User u : qr.getResultList()) {
			System.out.println(u.getId() + ":" + u.getName() + ":" + u.getExpireTime());
		}
	}
	
	@Test
	public void testReadByCondition() {
		testInertRAMIndex();
		testReadCommonByCondition(keyword);
		commonIndexManager.merge();
		testReadCommonByCondition(keyword);
	}
	
	
}
