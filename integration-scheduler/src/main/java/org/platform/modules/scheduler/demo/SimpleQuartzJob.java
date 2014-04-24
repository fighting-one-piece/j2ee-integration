package org.platform.modules.scheduler.demo;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

public class SimpleQuartzJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Scheduler scheduler = context.getScheduler();
		try {
			System.out.println("scheduler: " + scheduler.getSchedulerName());
			Trigger trigger = context.getTrigger();
			System.out.println("trigger: " + trigger.getClass());
			JobDetail jobDetail = context.getJobDetail();
			System.out.println("jobClass: " + jobDetail.getJobClass());
			System.out.println("jobKey: " + jobDetail.getKey());
			JobDataMap dataMap = jobDetail.getJobDataMap();
			System.out.println("jobDataMap: " + dataMap);
			if (!dataMap.isEmpty()) {
				System.out.println("param: " + dataMap.get("param"));
			}
			System.out.println("execute simple job");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
