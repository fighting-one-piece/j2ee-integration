package org.platform.modules.scheduler.spring;

import org.quartz.JobDetail;
import org.quartz.Trigger;

public interface IJobBusiness {

	public void addScheduleJob(JobDetail jobDetail, Trigger trigger);
}
