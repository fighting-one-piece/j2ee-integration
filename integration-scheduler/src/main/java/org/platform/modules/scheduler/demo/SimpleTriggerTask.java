package org.platform.modules.scheduler.demo;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleTriggerTask implements ITask {

	@Override
	public void execute() {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
//			JobDetailImpl jobDetail = new JobDetailImpl();
//			jobDetail.setName("jobDetailOne");
//			jobDetail.setGroup("jobGroupOne");
//			jobDetail.setJobClass(SimpleQuartzJob.class);
			JobDetail jobDetail = JobBuilder.newJob(SimpleQuartzJob.class)
					.withIdentity("jobDetailOne2", "jobGroupOne2").build();
//			SimpleTriggerImpl simpleTrigger = new SimpleTriggerImpl();
//			simpleTrigger.setName("simpleTriggerOne");
//			simpleTrigger.setGroup("triggerGroupOne");
//			simpleTrigger.setStartTime(new Date(System.currentTimeMillis()));
//			simpleTrigger.setRepeatCount(10);
//			simpleTrigger.setRepeatInterval(2);
			SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger()
					.withIdentity("simpleTriggerOne2", "triggerGroupOne2")
					.startAt(new Date(System.currentTimeMillis()))
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).withRepeatCount(10))
					.build();
			
//			CustomJobListener jobListener = new CustomJobListener();
//			List<Matcher<JobKey>> matchers = new ArrayList<Matcher<JobKey>>();
//			matchers.add(KeyMatcher.keyEquals(jobDetail.getKey()));
//			matchers.add(KeyMatcher.keyEquals(new JobKey("")));
//			scheduler.getListenerManager().addJobListener(jobListener, matchers);
			scheduler.scheduleJob(jobDetail, simpleTrigger);
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
		}
	}

}
