package org.platform.modules.scheduler.spring;

import javax.annotation.Resource;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdScheduler;
import org.springframework.stereotype.Service;

@Service("jobBusiness")
public class JobBusinessImpl implements IJobBusiness {
	
	@Resource(name = "scheduler")
	private StdScheduler scheduler = null;

	@Override
	public void addScheduleJob(JobDetail jobDetail, Trigger trigger) {
		try {
			if (!scheduler.isStarted()) scheduler.start();
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	
}
