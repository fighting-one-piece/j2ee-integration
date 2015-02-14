package org.platform.modules.scheduler.business.impl;

import java.util.List;

import javax.annotation.Resource;

import org.platform.modules.scheduler.business.ISchedulerBusiness;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("schedulerBusiness")
public class SchedulerBusinessImpl implements ISchedulerBusiness {
	
	private static final Logger LOG = LoggerFactory.getLogger(SchedulerBusinessImpl.class);
	
	@Resource(name = "schedulerFactory")
	private Scheduler scheduler = null;
	
	@Override
	public void log() {
		System.out.println("!!!Temp Log!!!");
	}
	
	@Override
	public void insert() {
		System.out.println("!!!start!!!");
		System.out.println("!!!scheduler!!!" + scheduler);
		try {
			String name = null, group = null, cron = null;
			CronExpression cronExpression = new CronExpression(cron);
			ScheduleBuilder<CronTrigger> cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			CronTrigger cronTrigger = TriggerBuilder.newTrigger().withSchedule(cronScheduleBuilder).build();
			JobDetail jobDetail = null;
			JobKey jobKey = new JobKey(name, group);
			if (scheduler.checkExists(jobKey)) {
				jobDetail = scheduler.getJobDetail(jobKey);
			} else {
				JobBuilder jobBuilder = JobBuilder.newJob().withIdentity(jobKey);
				jobDetail = jobBuilder.build();
			}
			scheduler.addJob(jobDetail, true);
			scheduler.scheduleJob(jobDetail, cronTrigger);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void pause(String name, String group) {
		JobKey jobKey = JobKey.jobKey(name, group);
		try {
			if (scheduler.checkExists(jobKey)) {
				scheduler.pauseJob(jobKey);
			}
		} catch (SchedulerException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void resume(String name, String group) {
		JobKey jobKey = JobKey.jobKey(name, group);
		try {
			if (scheduler.checkExists(jobKey)) {
				scheduler.resumeJob(jobKey);
			}
		} catch (SchedulerException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void delete(String name, String group) {
		JobKey jobKey = JobKey.jobKey(name, group);
		try {
			if (scheduler.checkExists(jobKey)) {
				scheduler.deleteJob(jobKey);
			}
		} catch (SchedulerException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void list(){
		try {
			List<JobExecutionContext> list = scheduler.getCurrentlyExecutingJobs();
			for (int i = 0, len = list.size(); i < len; i++) {
				JobExecutionContext jobExecutionContext = list.get(i);
				JobDetail jobDetail = jobExecutionContext.getJobDetail();
				System.out.println(jobDetail.getKey().getName());
				Trigger trigger = jobExecutionContext.getTrigger();
				System.out.println(trigger.getStartTime());
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
}
