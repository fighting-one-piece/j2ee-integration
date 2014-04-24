package org.platform.utils.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

	public static void main(String[] args) {
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
		ITask<String> task = new StringTask();
//		ExecutorService executorService = Executors.newCachedThreadPool();
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 50; i++) {
			executorService.submit(new Producer(queue, task));
			if (i % 5 == 0) {
				executorService.submit(new Consumer(queue, task));
			}
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
