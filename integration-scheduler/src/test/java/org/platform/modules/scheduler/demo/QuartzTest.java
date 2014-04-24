package org.platform.modules.scheduler.demo;

import org.junit.Test;
import org.platform.modules.scheduler.demo.CronTriggerTask;
import org.platform.modules.scheduler.demo.ITask;
import org.platform.modules.scheduler.demo.JobInfoTask;
import org.platform.modules.scheduler.demo.SimpleTriggerTask;
import org.platform.modules.scheduler.demo.TriggerInfoTask;
import org.platform.modules.scheduler.spring.IJobBusiness;
import org.platform.modules.scheduler.spring.MainContainer;
import org.platform.modules.scheduler.spring.SimpleJobOne;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
		MainContainer main = (MainContainer) context.getBean("mainContainer");
		main.startup(60);
	}
	
	@Test
	public void test6() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
		MainContainer main = (MainContainer) context.getBean("mainContainer");
		main.startup(30);
		IJobBusiness jobBusiness = (IJobBusiness) context.getBean("jobBusiness");
		JobDetail jobDetail = JobBuilder.newJob(SimpleJobOne.class).withIdentity("job2", "jobGroupOne").build();
		CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger()
				.withIdentity("trigger2", "triggerGroupOne")
				.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
				.build();
		jobBusiness.addScheduleJob(jobDetail, trigger);
		main.startup(60);
	}
}
