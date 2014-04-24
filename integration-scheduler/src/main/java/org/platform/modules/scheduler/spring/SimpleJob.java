package org.platform.modules.scheduler.spring;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component("simpleJob")
public class SimpleJob extends QuartzJobBean {
	
	private int timeout = 0;
	
	@Resource(name = "jobBusiness")
	private IJobBusiness job = null;
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		System.out.println("timeout: " + timeout);
		SchedulerContext schedulerContext = null;
		try {
			schedulerContext = jobExecutionContext.getScheduler().getContext();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		ApplicationContext context = (ApplicationContext) schedulerContext.get("applicationContext");
		MainContainer main = (MainContainer) context.getBean("mainContainer");
		main.print();
		IJobBusiness jobBusiness = (IJobBusiness) schedulerContext.get("jobBusiness");
		System.out.println("jobBusiness: " + jobBusiness);
		System.out.println("job: " + job);
	}

}
