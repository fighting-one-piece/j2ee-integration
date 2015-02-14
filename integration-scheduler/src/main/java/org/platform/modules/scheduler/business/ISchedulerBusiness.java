package org.platform.modules.scheduler.business;

public interface ISchedulerBusiness {

	public void log();
	
	public void insert();
	
	public void update();
	
	public void pause(String name, String group);
	
	public void resume(String name, String group);
	
	public void delete(String name, String group);
	
	public void list();
	
}
