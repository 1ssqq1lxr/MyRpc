package com.it.netty.rpc.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class ServiceRegist {
	  private CountDownLatch latch = new CountDownLatch(1);
	    private String registryAddress;
	    public  ServiceRegist(String registryAddress) {
	        this.registryAddress = registryAddress;
	    }
	    //注册到zk中，其中data为服务端的 ip:port
	    public void register(String data) {
	        if (data != null) {
	            ZooKeeper zk = connectServer();
	            if (zk != null) {
	                createNode(zk, data);
	            }
	        }
	    }
	    private ZooKeeper connectServer() {
	        ZooKeeper zk = null;
	        try {
	           zk = new ZooKeeper(registryAddress, 5000, new Watcher() {
	                public void process(WatchedEvent event) {
	                    if (event.getState() == Event.KeeperState.SyncConnected) {
	                        latch.countDown();
	                    }
	                }
	            });
	            latch.await();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return zk;
	    }
	    
	    private void createNode(ZooKeeper zk, String data) {
	        try {
	            byte[] bytes = data.getBytes();
	            String path = zk.create("/test", bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	            System.out.println("create zookeeper node path:"+path+" data:"+data);
	        } catch (Exception e) {
	         e.printStackTrace();
	        }
	    }
}
