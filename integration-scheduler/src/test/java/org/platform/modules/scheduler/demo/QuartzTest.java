package org.platform.modules.scheduler.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-scheduler.xml"})
public class QuartzTest {

	@Test
	public void test1() throws SchedulerException {
		ITask task = new SimpleTriggerTask();
		task.execute();
	}
	
	@Test
	public void test2() throws SchedulerException {
		ITask task = new CronTriggerTask();
		task.execute();
	}
	
	@Test
	public void test3() throws SchedulerException {
		ITask task = new TriggerInfoTask();
		task.execute();
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test4() throws SchedulerException {
		ITask task = new JobInfoTask();
		task.execute();
	}
	
	@Test
	public void test5() {
		try {
			Thread.sleep(1000 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
