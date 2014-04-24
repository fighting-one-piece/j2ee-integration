package org.platform.modules.crawl.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.platform.modules.crawl.service.ICrawlService;
import org.platform.utils.properties.PropertiesUtils;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstrCrawlServiceImpl implements ICrawlService {
	
	protected Logger logger = Logger.getLogger(getClass());
	
	private ExecutorService pools = Executors.newCachedThreadPool();
	
	private List<Thread> threads = new ArrayList<Thread>();
	
	@Override
	@Transactional
	public void start() {
		Properties properties = PropertiesUtils.newInstance("crawl/crawl.properties");
		int queue_size = Integer.parseInt(properties.getProperty("initial.queue.size"));
		int producer_size = Integer.parseInt(properties.getProperty("initial.producer.size"));
		int consumer_size = Integer.parseInt(properties.getProperty("initial.consumer.size"));
		
		initData(properties);
		
		BlockingQueue<?> queue = new BlockingArrayQueue<>(queue_size);
		
		Queue<Future<?>> futureQueues = new LinkedList<Future<?>>();
		
	    for (int i = 0; i < producer_size; i++) {
	    	Thread producerThread = new ProducerThread(queue);
	    	threads.add(producerThread);
	    	futureQueues.add(pools.submit(producerThread));
	    }
	    for (int j = 0; j < consumer_size; j++) {
	    	Thread consumerThread = new ConsumerThread(queue);
	    	threads.add(consumerThread);
	    	futureQueues.add(pools.submit(consumerThread));
	    }
	    
	    Iterator<Future<?>> iter = futureQueues.iterator();
		while(iter.hasNext()) {
			Future<?> future = iter.next();
			if (future.isDone()) {
				iter.remove();
			}
			iter = futureQueues.iterator();
		}
		logger.info("crawl finish");
	}

	@Override
	public void stop() {
		if (!pools.isShutdown()) pools.shutdownNow();
		for (Thread thread : threads) {
			if (!thread.isInterrupted()) {
				thread.interrupt();
			}
		}
	}
	
	protected void initData(Properties properties) {
		
	}
	
	protected void waitTime(long second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			logger.info(e.getMessage(), e);
		}
	}
	
	public abstract <T> void doProduce(BlockingQueue<T> queue);
	
	public abstract void doConsume(BlockingQueue<?> queue);
	
	private class ProducerThread extends Thread {
		
		private BlockingQueue<?> queue = null;
		
		private volatile boolean running = true;
		
		public ProducerThread(BlockingQueue<?> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				logger.info("start to produce");
				while(running) {
					waitTime(5);
					doProduce(queue);
				}
				logger.info("end to produce");
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
			}
		}
		
		@SuppressWarnings("unused")
		public void setRunning(boolean running) {
			this.running = running;
		}
		
	}
	
	private class ConsumerThread extends Thread {
		
		private BlockingQueue<?> queue = null;
		
		private volatile boolean running = true;
		
		public ConsumerThread(BlockingQueue<?> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				logger.info("start to consume");
				while (running) {
					waitTime(10);
					doConsume(queue);
				}
				logger.info("end to consume");
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
			}
		}
		
		@SuppressWarnings("unused")
		public void setRunning(boolean running) {
			this.running = running;
		}
		
	}

}
