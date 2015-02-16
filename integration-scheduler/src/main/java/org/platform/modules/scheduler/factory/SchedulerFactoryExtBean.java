package org.platform.modules.scheduler.factory;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class SchedulerFactoryExtBean extends SchedulerFactoryBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		if (isAutoStartup()) {
			super.afterPropertiesSet();
		}
	}
	
}
