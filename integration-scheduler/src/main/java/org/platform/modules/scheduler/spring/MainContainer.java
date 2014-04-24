package org.platform.modules.scheduler.spring;

import org.springframework.stereotype.Component;

@Component("mainContainer")
public class MainContainer {

	public void startup(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void print() {
		System.out.println("this is a main container");
	}
}
