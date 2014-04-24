package org.platform.modules.scheduler.demo;

import org.quartz.SchedulerException;

public interface ITask {

	public void execute() throws SchedulerException;
}
