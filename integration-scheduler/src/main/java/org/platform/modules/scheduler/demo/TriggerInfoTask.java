package org.platform.modules.scheduler.demo;

import java.util.List;
import java.util.Set;

import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;

public class TriggerInfoTask implements ITask {

	@Override
	public void execute() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
        for (String triggerGroupName : triggerGroupNames) {
        	System.out.println("triggerGroupName: " + triggerGroupName);
        	GroupMatcher<TriggerKey> matcher = GroupMatcher.triggerGroupEquals(triggerGroupName);
            Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(matcher);
            for (TriggerKey triggerKey : triggerKeys) {
            	System.out.println("triggerKey: " + triggerKey.getName());
            	Trigger trigger = scheduler.getTrigger(triggerKey);
            	System.out.println("trigger: " + trigger.getClass());
            	TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            	System.out.println("triggerState: " + triggerState.name());
            	if (trigger instanceof SimpleTrigger) {
            		SimpleTriggerImpl simpleTrigger = (SimpleTriggerImpl) trigger;
            		simpleTrigger.setRepeatCount(10);
            		simpleTrigger.setRepeatInterval(2);
            		scheduler.rescheduleJob(triggerKey, simpleTrigger);
            	} else if (trigger instanceof CronTrigger) {
            		CronTrigger cronTrigger = (CronTrigger) trigger;
            		scheduler.rescheduleJob(triggerKey, cronTrigger);
            	}
            }
        }        
        scheduler.start();
	}

	
}
