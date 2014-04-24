package org.platform.utils.thread;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

	@SuppressWarnings("rawtypes")
	private BlockingQueue queue = null;
	
	@SuppressWarnings("rawtypes")
	private ITask task = null;
	
	public Producer(BlockingQueue<?> queue) {
		this.queue = queue;
	}
	
	public Producer(BlockingQueue<?> queue, ITask<?> task) {
		this.queue = queue;
		this.task = task;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " producer start");
		try {
			queue.put(task.produce());
			System.out.println("producer queue size: " + queue.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " producer end");
	}
	
	
}
