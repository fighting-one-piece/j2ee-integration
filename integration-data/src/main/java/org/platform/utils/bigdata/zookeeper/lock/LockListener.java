package org.platform.utils.bigdata.zookeeper.lock;

public interface LockListener {
	
	/** 锁获取*/
    public void lockAcquired();
    
    /** 锁释放*/
    public void lockReleased();

}
