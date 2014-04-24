package org.platform.utils.thread;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

	@SuppressWarnings("rawtypes")
	private BlockingQueue queue = null;
	
	@SuppressWarnings("rawtypes")
	private ITask task = null;
	
	public Consumer(BlockingQueue<?> queue) {
		this.queue = queue;
	}
	
	public Consumer(BlockingQueue<?> queue, ITask<?> task) {
		this.queue = queue;
		this.task = task;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " consume start");
		Object object;
		try {
			object = queue.take();
			while (null != object) {
				task.consume(object);
				System.out.println("consumer queue size: " + queue.size());
				object = queue.take();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " consume end");
	}
	
}
