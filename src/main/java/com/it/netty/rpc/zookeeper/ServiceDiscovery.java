package com.it.netty.rpc.zookeeper;

import io.netty.util.internal.ThreadLocalRandom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ServiceDiscovery {
	private CountDownLatch latch = new CountDownLatch(1);
	private volatile Map<String,Object> dataList = new ConcurrentHashMap<String,Object>();
	private String registryAddress;

	public ServiceDiscovery(String registryAddress) {
		this.registryAddress = registryAddress;
		ZooKeeper zk = connectServer();
		if (zk != null) {
			watchNode(zk);
		}
	}

	private ZooKeeper connectServer() {
		ZooKeeper zk = null;
		try {
			//format host1:port1,host2:port2,host3:port3
			zk = new ZooKeeper(registryAddress, 5000, new Watcher() {
				public void process(WatchedEvent event) {
					//zookeeper处于同步连通的状态时
					if (event.getState() == Event.KeeperState.SyncConnected) {
						latch.countDown();
					}
				}
			});
			latch.await();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return zk;
	}
	private void watchNode(final ZooKeeper zk) {
		try {
			List<String> nodeList = zk.getChildren("/", new Watcher() {
				public void process(WatchedEvent event) {
					if (event.getType() == Event.EventType.NodeChildrenChanged) {
						watchNode(zk);
					}
				}
			});
			for (String node : nodeList) {
				byte[] bytes = zk.getData( "/" + node, false, null);
				dataList.put(node, new String(bytes));
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		ServiceDiscovery discovery = new ServiceDiscovery("localhost:12181");
	}
}
