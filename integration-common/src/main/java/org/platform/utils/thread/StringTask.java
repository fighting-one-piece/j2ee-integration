package org.platform.utils.thread;

import org.platform.utils.random.RandomUtils;

public class StringTask extends AbstrTask<String> {

	@Override
	public String produce() {
		String randomStr = RandomUtils.generateUUID();
		System.out.println(Thread.currentThread().getName() + " produce: " + randomStr);
		return randomStr;
	}
	
	@Override
	public void consume(String t) {
		System.out.println(Thread.currentThread().getName() + " consume: " + t);
	}
	
}
