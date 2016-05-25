package org.platform.utils.bigdata;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import org.platform.utils.bigdata.zookeeper.ZookeeperUtils;

public class ZookeeperUtilsTest {
	
	@Test
	public void test01() {
		testList();
		testCreate();
	}
	
	@Test
	public void test02() {
		testCreate();
	}
	
	
	@Test
	public void testCreate() {
		String connectString = "192.168.10.20:2181";
		ZooKeeper zk = ZookeeperUtils.obtainZookeeper(connectString, 20);
		System.out.println("sessionId: " + zk.getSessionId());
		try {
//			zk.create("/tmp", "tmp".getBytes(), Ids.OPEN_ACL_UNSAFE, 
//					CreateMode.PERSISTENT);
			String id = zk.create("/tmp/lock", "lock".getBytes(), Ids.OPEN_ACL_UNSAFE, 
					CreateMode.EPHEMERAL_SEQUENTIAL);
			System.out.println("id: " + id);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testList() {
		String connectString = "192.168.10.20:2181";
		ZooKeeper zk = ZookeeperUtils.obtainZookeeper(connectString, 20);
		System.out.println("sessionId: " + zk.getSessionId());
		try {
			List<String> children = zk.getChildren("/tmp", new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					System.out.println(event.getState());
					EventType eventType = event.getType();
					if (eventType == EventType.None) {
						System.out.println("event type none");
					} else if (eventType == EventType.NodeChildrenChanged) {
						System.out.println("node children changed");
					} 
				}
			});
			for (int i = 0, size = children.size(); i < size; i++) {
				System.out.println("child: " + children.get(i));
			}
			Thread.sleep(100000000);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void crud() throws Exception {
		ZooKeeper zk = ZookeeperUtils.obtainZookeeper();
		// 创建一个目录节点
		zk.create("/testRootPath", "testRootData".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		// 创建一个子目录节点
		zk.create("/testRootPath/testChildPathOne",
				"testChildDataOne".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		System.out.println(new String(zk.getData("/testRootPath", false, null)));
		// 取出子目录节点列表
		System.out.println(zk.getChildren("/testRootPath", true));
		// 修改子目录节点数据
		zk.setData("/testRootPath/testChildPathOne", "modifyChildDataOne".getBytes(), -1);
		System.out.println("目录节点状态：[" + zk.exists("/testRootPath", true) + "]");
		// 创建另外一个子目录节点
		zk.create("/testRootPath/testChildPathTwo",
				"testChildDataTwo".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
		System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo", true, null)));
		// 删除子目录节点
		zk.delete("/testRootPath/testChildPathTwo", -1);
		zk.delete("/testRootPath/testChildPathOne", -1);
		// 删除父目录节点
		zk.delete("/testRootPath", -1);
		// 关闭连接
		zk.close();
	}
}
