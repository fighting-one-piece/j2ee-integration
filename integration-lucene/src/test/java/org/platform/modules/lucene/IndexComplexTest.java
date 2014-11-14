package org.platform.modules.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
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
import org.platform.modules.lucene.ComplexIndexManager;
import org.platform.modules.lucene.FSIndexManager;
import org.platform.modules.lucene.IIndex;
import org.platform.modules.lucene.IIndexManager;
import org.platform.modules.lucene.IndexController;
import org.platform.modules.lucene.RAMIndexManager;
import org.platform.utils.random.RandomUtils;

public class IndexComplexTest {
	
	private IIndexManager fsIndexManager = null;
	
	private IIndexManager ramIndexManager = null;
	
	private IIndexManager complexIndexManager = null;
	
	@Before
	public void before() {
		fsIndexManager = new FSIndexManager();
		ramIndexManager = new RAMIndexManager();
		complexIndexManager = new ComplexIndexManager();
	}

	public void insertFSIndex(String keyword) {
		System.out.println("insert fs index start");
		Calendar calendar = Calendar.getInstance();
		Random random = new Random();
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i < 2; i++) {
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
		fsIndexManager.commit();
		System.out.println("insert fs index end");
	}
	
	public void insertRAMIndex(String keyword) {
		System.out.println("insert ram index start");
		Calendar calendar = Calendar.getInstance();
		Random random = new Random();
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i < 2; i++) {
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
		complexIndexManager.insert(userList.toArray(new Object[0]));
		//ramIndexManager.insert(userList.toArray(new Object[0]));
		//ramIndexManager.commit();
		System.out.println("insert ram index end");
	}
	
	public void deleteIndex(String keyword) {
		System.out.println("delete index start");
		for (int i = 0; i < 5; i++) {
			fsIndexManager.delete(new Term("name", keyword + i));
		}
		//鍒犻櫎涓嶆彁浜や粛鐒跺彲浠ユ煡璇㈠嚭缁撴灉
		//fsIndexManager.commit();
		System.out.println("delete index end");
	}
	
	@SuppressWarnings("unchecked")
	public void readFSByCondition(String keyword) {
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
	public void readRAMByCondition(String keyword) {
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
	public void readCommonByCondition(String keyword) {
		Query condition = new Query();
		condition.addCondition(Query.CURRENT_PAGE_NUM, 0);
		condition.addCondition(Query.ROW_NUM_PER_PAGE, 10);
		condition.addCondition(Query.LUCENE_CLASS, User.class);
		//condition.addCondition(QueryCondition.LUCENE_KEYWORD, "google");
		condition.addCondition(Query.LUCENE_QUERY, new WildcardQuery(new Term("name", keyword + "*")));
		//condition.addCondition(QueryCondition.LUCENE_ANALYZER, IndexController.getInstance().obtainAnalyzer(IIndex.ANALYZER_MMSEG4J_MAXWORD));
		Sort sort = new Sort(new SortField("name", Type.STRING));
		condition.addCondition(Query.LUCENE_SORT, sort);
		condition.addCondition(Query.LUCENE_HIGHLIGHTER_FIELDS, new String[]{"id", "name"});
		QueryResult<User> qr = (QueryResult<User>) complexIndexManager.readByCondition(condition);
		System.out.println("common query total row number: " + qr.getTotalRowNum());
		for (User u : qr.getResultList()) {
			System.out.println(u.getId() + ":" + u.getName() + ":" + u.getExpireTime());
		}
	}
	
	@Test
	public void testInsertFSIndex() {
		insertFSIndex("111");
		readCommonByCondition("111");
		insertFSIndex("222");
		readCommonByCondition("222");
	}
	
	@Test
	public void testInsertRAMIndex() {
		insertRAMIndex("1111");
		readCommonByCondition("1111");
		insertRAMIndex("2222");
		readCommonByCondition("2222");
	}
	
	@Test
	public void testIndexAnalyzer() {
		String word = "鎴戞槸涓崕浜烘皯鍏卞拰鍥藉叕姘?鎴戝嚭鐢熷湪鍥涘窛,鎴戝湪鏂扮枂闀垮ぇ";
		Analyzer analyzer = IndexController.getInstance().obtainAnalyzer(
				IIndex.ANALYZER_MMSEG4J_MAXWORD);
		try {
			TokenStream tokenStream = analyzer.tokenStream("field", new StringReader(word));
			CharTermAttribute charTermAttr = tokenStream.addAttribute(CharTermAttribute.class);
			PositionLengthAttribute positionLengthAttr = tokenStream.addAttribute(PositionLengthAttribute.class);
			PositionIncrementAttribute positionIncAttr = tokenStream.addAttribute(PositionIncrementAttribute.class);
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				System.out.print(charTermAttr.toString());
				System.out.print(positionLengthAttr.getPositionLength() + ":");
				System.out.println(positionIncAttr.getPositionIncrement());
			}
			tokenStream.end();
			tokenStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadByCondition() {
		String keyword = "aaa";
		//insertRAMIndex(keyword);
		readCommonByCondition(keyword);
	}
	
	
	@Test
	public void testComplexReadByCondition() {
		String keyword = "zzz";
		insertRAMIndex(keyword);
		readCommonByCondition(keyword);
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<?> result = executorService.submit(new Runnable() {
			@Override
			public void run() {
				complexIndexManager.merge();
				readCommonByCondition("zzz");
			}
			
		});
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		readCommonByCondition(keyword);
		insertRAMIndex("five");
		readCommonByCondition("five");
		while (!result.isDone()) {
			
		}
		System.out.println("finish");
	}
	
	//3涓嚎绋嬪幓鍋氬垱寤虹储寮?11涓嚎绋嬪幓鍋氭煡璇㈢储寮?1涓嚎绋嬪幓鍋氭洿鏂?
	@Test
	public void testMultiThreadIndex() {
		Queue<Future<?>> queue = new ArrayBlockingQueue<Future<?>>(15);
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 3; i++) {
			Future<?> writeF = executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(new Random().nextInt(8) * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + " write index");
					insertRAMIndex(RandomUtils.generateUUID());
				}
			});
			queue.add(writeF);
		}
		for (int j = 0; j < 11; j++) {
			final String keyword = j + "";
			Future<?> readF = executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(new Random().nextInt(4) * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + " read index");
					readCommonByCondition(keyword);
				}
			});
			queue.add(readF);
		}
		for (int k = 0; k < 1; k++) {
			Future<?> updateF = executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(new Random().nextInt(8) * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + " update index");
					complexIndexManager.merge();
				}
			});
			queue.add(updateF);
		}
		Future<?> future = queue.poll();
		while (null != future) {
			if (!future.isDone()) queue.offer(future);
			future = queue.poll();
		}
		System.out.println("finished");
	}
	
	
}
