package org.platform.modules.scheduler.task;

import org.platform.modules.scheduler.business.ISchedulerBusiness;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TempTask extends QuartzJobBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(TempTask.class);
	
	private int timeout = 0;
	
	private String keyword = null;
	
	private ISchedulerBusiness schedulerBusiness = null;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public ISchedulerBusiness getSchedulerBusiness() {
		return schedulerBusiness;
	}

	public void setSchedulerBusiness(ISchedulerBusiness schedulerBusiness) {
		this.schedulerBusiness = schedulerBusiness;
	}

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		System.out.println("timeout: " + timeout);
		try {
			SchedulerContext schedulerContext = jobExecutionContext.getScheduler().getContext();
			ApplicationContext ac = (ApplicationContext) schedulerContext.get("applicationContext");
			ISchedulerBusiness schedulerBusiness = ac.getBean("schedulerBusiness", ISchedulerBusiness.class);
			System.out.println("AC SchedulerBusiness: " + schedulerBusiness);
			System.out.println("!!!SchedulerBusiness!!!: " + schedulerBusiness);
			System.out.println("!!!timeout!!!: " + timeout);
			System.out.println("!!!keyword!!!: " + keyword);
		} catch (SchedulerException e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
