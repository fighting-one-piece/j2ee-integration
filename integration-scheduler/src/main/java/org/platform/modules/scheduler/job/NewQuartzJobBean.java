package org.platform.modules.scheduler.job;

import java.lang.reflect.Method;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class NewQuartzJobBean extends QuartzJobBean {

	private static final Logger LOG = LoggerFactory.getLogger(NewQuartzJobBean.class);

	private String targetObject = null;
	private String targetMethod = null;
	private ApplicationContext ctx = null;

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			LOG.info("execute [" + targetObject + "] at once>>>>>>");
			Object otargetObject = ctx.getBean(targetObject);
			Method method = null;
			try {
				method = otargetObject.getClass().getMethod(targetMethod, new Class[] {});
				method.invoke(otargetObject, new Object[]{});
			} catch (SecurityException e) {
				LOG.error(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				LOG.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.ctx = applicationContext;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

}