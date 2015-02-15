package org.platform.modules.scheduler.business;

import java.util.List;

import org.platform.modules.scheduler.dto.JobDTO;
import org.platform.utils.exception.BusinessException;

public interface ISchedulerBusiness {

	/**
	 * 新增Job
	 * @param jobGroup
	 * @param jobName
	 * @param jobClass
	 * @param triggerGroup
	 * @param triggerName
	 * @param cron
	 * @throws BusinessException
	 */
	public void insert(String jobGroup, String jobName, String jobClass, String triggerGroup,
			String triggerName, String cron) throws BusinessException;
	
	/**
	 * 更新Job的Cron表达式
	 * @param jobGroup
	 * @param jobName
	 * @param cron
	 * @throws BusinessException
	 */
	public void updateJobCron(String jobGroup, String jobName, String cron) throws BusinessException; 
	
	/**
	 * 更新Trigger
	 * @param triggerGroup
	 * @param triggerName
	 * @param cron
	 * @throws BusinessException
	 */
	public void updateTrigger(String triggerGroup,String triggerName, String cron) throws BusinessException;
	
	/**
	 * 暂停Job
	 * @param jobGroup
	 * @param jobName
	 * @throws BusinessException
	 */
	public void pauseJob(String jobGroup, String jobName) throws BusinessException;
	
	/**
	 * 暂停Trigger
	 * @param triggerGroup
	 * @param triggerName
	 * @throws BusinessException
	 */
	public void pauseTrigger(String triggerGroup, String triggerName) throws BusinessException;
	
	/**
	 * 恢复Job
	 * @param jobGroup
	 * @param jobName
	 * @throws BusinessException
	 */
	public void resumeJob(String jobGroup, String jobName) throws BusinessException;
	
	/**
	 * 恢复Trigger
	 * @param triggerGroup
	 * @param triggerName
	 * @throws BusinessException
	 */
	public void resumeTrigger(String triggerGroup, String triggerName) throws BusinessException;
	
	/**
	 * 删除Job
	 * @param jobGroup
	 * @param jobName
	 * @throws BusinessException
	 */
	public void delete(String jobGroup, String jobName) throws BusinessException;
	
	/**
	 * 读取Job
	 * @param jobGroup
	 * @param jobName
	 * @return
	 * @throws BusinessException
	 */
	public JobDTO readJob(String jobGroup, String jobName) throws BusinessException;
	
	/**
	 * 读取Job列表
	 * @return
	 * @throws BusinessException
	 */
	public List<JobDTO> readJobs() throws BusinessException;
	
}
