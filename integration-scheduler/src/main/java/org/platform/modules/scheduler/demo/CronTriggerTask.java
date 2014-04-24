package org.platform.modules.scheduler.demo;

import java.text.ParseException;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class CronTriggerTask implements ITask {

	@Override
	public void execute() {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			if (scheduler.checkExists(new JobKey("jobDetailTwo4", "jobGroupTwo2"))) {
				System.out.println("job has existed");
				return;
			}
//			JobDetailImpl jobDetail = new JobDetailImpl();
//			jobDetail.setName("jobDetailTwo");
//			jobDetail.setGroup("jobGroupTwo");
//			jobDetail.setJobClass(SimpleQuartzJob.class);
			JobDetail jobDetail = JobBuilder.newJob(SimpleQuartzJob.class)
					.withIdentity("jobDetailTwo4", "jobGroupTwo2")
					.usingJobData("param", "job")
					.requestRecovery()
					.build();
//			CronTriggerImpl cronTrigger = new CronTriggerImpl();
//			cronTrigger.setName("cronTriggerOne");
//			cronTrigger.setGroup("cronGroupOne");
			CronExpression cronExpression = new CronExpression("0/5 * * * * ?");
//			cronTrigger.setCronExpression(cronExpression);
			CronTrigger cronTrigger = (CronTrigger) TriggerBuilder.newTrigger()
					.withIdentity("cronTriggerOne4", "cronGroupOne2")
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
					.build();
			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			scheduler.shutdown(true);
			SchedulerMetaData metaData = scheduler.getMetaData();
		    System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
