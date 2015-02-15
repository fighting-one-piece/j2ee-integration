package org.platform.modules.scheduler.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-scheduler.xml"})
public class QuartzTest {

	@Test
	public void test01() {
		try {
			Thread.sleep(1000 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
