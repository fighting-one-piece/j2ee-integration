package org.platform.utils.bigdata.zookeeper.lock;

import org.apache.zookeeper.KeeperException;

public interface ZooKeeperOperation {
	
	public boolean execute() throws KeeperException, InterruptedException;

}
