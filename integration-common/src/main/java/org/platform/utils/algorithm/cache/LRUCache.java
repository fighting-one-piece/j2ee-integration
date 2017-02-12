package org.platform.utils.algorithm.cache;

import java.util.Hashtable;  
import java.util.concurrent.CountDownLatch;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  

/**
 * ClassName:LRUCache <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年7月3日 上午10:53:45 <br/>
 * @author   wulin
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class LRUCache<K, V> {  
  
    private LinkCacheNode firstNode;  
  
    private LinkCacheNode lastNode;  
  
    private final int MAX_CACHE_SIZE;  
  
    private Hashtable<K, LinkCacheNode> hashtable;  
  
    public LRUCache(int cacheSize) {  
        this.MAX_CACHE_SIZE = cacheSize;  
        hashtable = new Hashtable<K, LinkCacheNode>(cacheSize);  
    }  
  
    /** 
     * 内部类,定义节点对象 
     *  
     * @author tanjie 
     * 
     */  
    class LinkCacheNode {  
        // 前一个节点  
        private LinkCacheNode prev;  
        // 下一个节点  
        private LinkCacheNode next;  
        // 当前节点的key  
        private K key;  
        // 当前节点的value  
        private V value;  
    }  
  
    // 当往链表里面放数据时,将最新的数据指向链表头  
    public synchronized void put(K key, V value) {  
        // 如果此时链表已经超过了最大的大小,则首先移除链表尾部的数据  
        LinkCacheNode cacheNode = getCacheNode(key);  
        if (null == cacheNode) {  
            // 如果链表已经不允许放数据  
            if (hashtable.size() >= MAX_CACHE_SIZE) {  
                if (null != lastNode) {  
                    hashtable.remove(lastNode.key);  
                    // 当缓存容量满的时候,移除链表尾部的数据  
                    removeLastNode();  
                }  
            }  
            cacheNode = new LinkCacheNode();  
        }  
        cacheNode.key = key;  
        // 如果连接里面有值,直接修改value值即可,key不变  
        cacheNode.value = value;  
        // 新加入的移到链表头部  
        moveToHeadNode(cacheNode);  
        hashtable.put(key, cacheNode);  
        System.out.println("当前线程:" + Thread.currentThread().getName()  + ",put操作:firstNode:" + firstNode);  
        System.out.println("当前线程:" + Thread.currentThread().getName() +",put操作:lastNode:" + lastNode);  
        System.out.println("=================================================");  
    }  
  
    /** 
     * 节点的获取,模拟每次获取一次,都将该节点移动到链表头,表示最近刚被访问过 
     *  
     * @param key 
     * @return 
     */  
    public synchronized Object get(K key) {  
        LinkCacheNode node = getCacheNode(key);  
        if (null == node) {  
            return null;  
        }  
        // 每次key被请求一次,就移动到链表头,模拟当前数据刚被访问过  
        moveToHeadNode(node);  
        System.out.println("当前线程:" + Thread.currentThread().getName() + ",get操作:firstNode:" + firstNode);  
        System.out.println("当前线程:" + Thread.currentThread().getName() + ",get操作:lastNode:" + lastNode);  
        System.out.println("================================");  
        return node.value;  
    }  
  
    /** 
     * 节点的删除 
     *  
     * @param key 
     */  
    public synchronized void remove(K key) {  
        LinkCacheNode entry = getCacheNode(key);  
        if (null != entry) {  
            // 设置前一个节点的引用  
            if (null != entry.prev) {  
                entry.prev.next = entry.next;  
            }  
            // 反向设置下一个节点的引用  
            if (null != entry.next) {  
                entry.next.prev = entry.prev;  
            }  
            // 如果删除的是链表头部节点,将下一个节点设置为第一个节点  
            if (entry == firstNode) {  
                firstNode = entry.next;  
            }  
            // 如果删除的是链表尾部节点,将前一个节点设置为最后一个节点  
            if (entry == lastNode) {  
                lastNode = entry.prev;  
            }  
        }  
        hashtable.remove(key);  
    }  
  
    /** 
     * 将节点移动到链表头 
     * @param cacheEntry 
     */  
    private void moveToHeadNode(LinkCacheNode cacheEntry) {  
        // 如果当前需要移动的就是第一个位置,则不需要移动  
        if (cacheEntry == firstNode) {  
            return;  
        }  
        // 在移动节点之前,先设置当前节点的前后节点的引用  
        if (null != cacheEntry.prev) {  
            cacheEntry.prev.next = cacheEntry.next;  
        }  
        if (null != cacheEntry.next) {  
            cacheEntry.next.prev = cacheEntry.prev;  
        }  
        // 如果是最后一个节点移动到头部,将节点的前一个设置为尾部节点  
        if (cacheEntry == lastNode) {  
            lastNode = cacheEntry.prev;  
        }  
        if (null != firstNode) {  
            // 移动节点的下一个节点的引用指向未移动前的链表的第一个节点  
            cacheEntry.next = firstNode;  
            // 未移动前链表的第一个节点的前一个节点的引用指向当前移动的节点  
            firstNode.prev = cacheEntry;  
        }  
        // 将移动节点设置为链表头部节点  
        firstNode = cacheEntry;  
        cacheEntry.prev = null;  
        if (null == lastNode) {  
            lastNode = firstNode;  
        }  
  
    }  
  
    /** 
     * 移除链表尾的数据 
     */  
    private void removeLastNode() {  
        if (null != lastNode) {  
            if (null != lastNode.prev) {  
                lastNode.prev.next = null;  
                // 只有一个节点的时候  
            } else {  
                firstNode = null;  
            }  
            lastNode = lastNode.prev;  
        }  
    }  
  
    /** 
     * 通过key获取节点数据对象 
     *  
     * @param key 
     * @return 
     */  
    private LinkCacheNode getCacheNode(K key) {  
        return hashtable.get(key);  
    }  
  
    public static void main(String[] args) throws InterruptedException {  
        final LRUCache<String, String> lru =   
                new LRUCache<String, String>(3);  
        final CountDownLatch latch = new CountDownLatch(15);  
        ExecutorService service = Executors.newCachedThreadPool();  
        for(int i=1;i<=15;i++){  
            final int index = i;  
            service.submit(new  Runnable() {  
                @Override  
                public void run() {  
                    lru.put(String.valueOf(index), "value:" + index);  
                    latch.countDown();  
                }  
            });  
        }  
        latch.await();                 
    }  

}

