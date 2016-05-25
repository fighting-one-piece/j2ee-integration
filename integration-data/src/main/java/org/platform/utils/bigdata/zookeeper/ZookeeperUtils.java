package org.platform.utils.bigdata.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperUtils {
	
	public static final String SERVER_ADDRESS = "192.168.10.10";

	public static final String SERVER_PORT = "2181";
	
	public static final String SERVER_ADDRESSES = 
			"192.168.10.10:2181,192.168.10.11:2181,192.168.10.12:2181";
	
	public static ZooKeeper obtainZookeeper() {
		ZooKeeper zookeeper = null;
		try {
			zookeeper = new ZooKeeper(SERVER_ADDRESSES, 10 * 1000, new Watcher() { 
			    // 监控所有被触发的事件
			    public void process(WatchedEvent event) { 
			        System.out.println("已经触发了 " + event.getType() + " 事件！"); 
			    } 
			});
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return zookeeper;
	}
	
	public static ZooKeeper obtainZookeeper(String connectString, int sessionTimeout) {
		ZooKeeper zookeeper = null;
		try {
			zookeeper = new ZooKeeper(connectString, sessionTimeout * 1000, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					KeeperState kstate = event.getState();
					System.out.println("已经触发了 " + event.getType() + kstate.name() + " 事件！"); 
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return zookeeper;
	}
}
