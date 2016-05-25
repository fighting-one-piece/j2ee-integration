package org.platform.modules.crawl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.jetty.util.ArrayQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.platform.utils.http.HttpUnitUtils;

public class CrawlTest {
	
	@Test
	public void testGetUrl() {
//		String url = "http://search.51job.com/list/090200%252C00,000000,0000,00,9,99,java%25B9%25A4%25B3%25CC%25CA%25A6,0,1.html?lang=c&stype=2&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&lonlat=0%2C0&radius=-1&ord_field=0&list_type=0&confirmdate=9&dibiaoid=0";
		String url = "http://search.51job.com/jobsearch/search_result.php?fromJs=1&jobarea=090200%2C00&district=000000&funtype=0000&industrytype=00&issuedate=9&providesalary=99&keyword=java%E5%B7%A5%E7%A8%8B%E5%B8%88&keywordtype=0&curr_page=1&lang=c&stype=2&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&lonlat=0%2C0&radius=-1&ord_field=0&list_type=0&fromType=14&dibiaoid=0&confirmdate=9";
		String html = HttpUnitUtils.get(url);
//		System.out.println("------html------");
//		System.out.println(html);
		Document document = Jsoup.parse(html);
		System.out.println(document);
	}
	
	@Test
	public void testGet() {
		String url = "http://www.youku.com/";
		String html = HttpUnitUtils.get(url);
//		System.out.println("------html------");
//		System.out.println(html);
		Document document = Jsoup.parse(html);
		Elements divElements = document.select("div.v-meta");
		System.out.println("------div start------");
		for (Element element : divElements) {
//				Elements spanElements = element.select("span.v-num");
//				System.out.println("------span------");
//				for (Element spanElement : spanElements) {
//					String spanElementText = spanElement.text();
//					if (null != spanElementText && !"".equals(spanElementText)) {
//						System.out.println(spanElementText);
//					}
//				}
			print(element);
			for (Element child : element.children()) {
				print(child);
			}
		}
		System.out.println("------div end------");
//		Elements spanElements = document.select("span.v-num");
//		System.out.println("------span------");
//		for (Element element : spanElements) {
//			String elementText = element.text();
//			if (null != elementText && !"".equals(elementText)) {
//				System.out.println(element.text());
//			}
//		}
	}
	
	@Test
	public void testGet1() throws IOException {
//		Document document = Jsoup.parse(new File("D:\\a.txt"), "UTF-8");
		String url = "http://www.youku.com/";
		String html = HttpUnitUtils.get(url);
		Document document = Jsoup.parse(html);
		Elements divElements = document.select("div.v-meta");
		System.out.println("------div start------");
		for (Element element : divElements) {
//			print(element);
			Elements titleElements = element.select("div.v-meta-title a");
			for (Element titleElement : titleElements) {
				print(titleElement);
			}
			Elements commentElements = element.select("div.v-meta-entry span");
			for (Element commentElement : commentElements) {
				print(commentElement);
			}
			Elements numElements = element.select("div.v-meta-tagrt span.v-num");
			for (Element numElement : numElements) {
				print(numElement);
			}
		}
		System.out.println("------div end------");
	}
	
	private void print(Element element) {
		System.out.println("------element start------");
		String elementText = element.text();
		if (null != elementText && !"".equals(elementText)) {
			System.out.println("text: " + elementText);
//			System.out.println("outerHtml: " + element.outerHtml());
		}
		System.out.println("------element end------");
	}
	
//	private void print(Element element, int level) {
//		System.out.println("------level------" + level + "------");
//		System.out.println("outerHtml: " + element.outerHtml());
//		Elements elements = element.children();
//		while (null != elements) {
//			for (Element child : elements) {
//				print(child, level + 1);
//			}
//		}
//		
//	}
	
	@Test
	public void test() throws InterruptedException, ExecutionException {
		ExecutorService pools = Executors.newCachedThreadPool();
		Future<Boolean> f = pools.submit(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				throw new RuntimeException("hahahaha");
//				return Boolean.FALSE;
			}
		});
		
		System.out.println(f.get());
		System.out.println(f.isDone());
	}
	
	@Test
	public void test1() {
		ExecutorService pools = Executors.newCachedThreadPool();
		
		Queue<Future<?>> queues = new ArrayQueue<Future<?>>();
		
		for (int i = 0; i < 10; i++) {
			final int j = i;
			Future<?> future = pools.submit(new Runnable() {
				
				@Override
				public void run() {
					try {
						int second = new Random().nextInt(10);
						System.out.println(j + " " + Thread.currentThread().getName() + " sleep " + second);
						Thread.sleep(second * 1000);
						System.out.println(j + " " + Thread.currentThread().getName() + " sleep " + second + " finish");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			queues.add(future);
		}
		
		Iterator<Future<?>> iter = queues.iterator();
		while(iter.hasNext()) {
			Future<?> future = iter.next();
			if (future.isDone()) {
				iter.remove();
			}
			iter = queues.iterator();
		}
	}
	
	ExecutorService pools = Executors.newCachedThreadPool();
	
	Queue<Task> tasks = new ArrayQueue<Task>();
	
	@Test
	public void test2() {
		Queue<Future<?>> queues = new ArrayQueue<Future<?>>();
		
		for (int i = 0; i < 3; i++) {
			Task task = new Task(i);
			tasks.add(task);
			Future<?> future = pools.submit(task);
			queues.add(future);
		}
		
		Iterator<Future<?>> iter = queues.iterator();
		while(iter.hasNext()) {
			Future<?> future = iter.next();
			if (future.isDone()) {
				iter.remove();
			}
			iter = queues.iterator();
		}
	}
	
	@Test
	public void test3() {
		pools.shutdownNow();
		for (Task task : tasks) {
			task.setRunning(false);
		}
	}
	
	private class Task implements Runnable {
		
		private int j = 0;
		
		private volatile boolean running = true;
		
		public Task(int j) {
			this.j = j;
		}
		
		public void setRunning(boolean running) {
			this.running = running;
		}
		
		@Override
		public void run() {
			try {
				int second = new Random().nextInt(10);
				System.out.println(j + " " + Thread.currentThread().getName() + " sleep " + second);
				Thread.sleep(second * 1000);
				while(running) {
					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		int a = 1, b = 2;
		a = a + b;
		b = a - b;
		a = a - b;
		System.out.println("a: " + a);
		System.out.println("b: " + b);
		int c = 1, d = 2;
		d = c ^ d;
		c = c ^ d;
		d = c ^ d;
		System.out.println("c: " + c);
		System.out.println("d: " + d);
		System.out.println(Math.ceil(10/2));
		System.out.println(Math.ceil(10/3));
		System.out.println(Math.ceil(10/4));
	}

}
