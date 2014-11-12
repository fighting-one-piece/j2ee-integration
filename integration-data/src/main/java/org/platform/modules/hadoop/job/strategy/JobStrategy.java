package org.platform.modules.hadoop.job.strategy;

import org.platform.modules.hadoop.job.AbstractJob;

public abstract interface JobStrategy {
	
	public abstract int rubJob(AbstractJob job);

}
