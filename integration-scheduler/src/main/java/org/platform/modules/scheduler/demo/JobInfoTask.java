package org.platform.modules.scheduler.demo;

import java.util.List;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

public class JobInfoTask implements ITask {

	@Override
	public void execute() throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		List<String> jobGroupNames = scheduler.getJobGroupNames();
		for (String jobGroupName : jobGroupNames) {
			System.out.println("jobGroupName: " + jobGroupName);
			GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals(jobGroupName);
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			for (JobKey jobKey : jobKeys) {
				System.out.println("jobKey: " + jobKey.getName());
				JobDetail jobDetail = scheduler.getJobDetail(jobKey);
				System.out.println("jobDetail: " + jobDetail.getKey());
			}
		}
	}

}
