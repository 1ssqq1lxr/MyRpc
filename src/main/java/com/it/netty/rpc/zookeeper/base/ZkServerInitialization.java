package com.it.netty.rpc.zookeeper.base;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.zookeeper.ServiceDiscovery;
import com.it.netty.rpc.zookeeper.ServiceRegist;

public class ZkServerInitialization implements BaseZkClient {
	private static String registryAddress;
	private CountDownLatch latch = new CountDownLatch(1);
	protected Logger logger = LoggerFactory.getLogger(getClass());
	ZooKeeper zk ;
	private ZkServerInitialization(){
		zk= connectServer();
	}

	public void registURI(String path, URI uri) {
		if(zk!=null){
			// TODO Auto-generated method stub
			ServiceRegist.register( zk,path,  uri);	
		}
	}
	
	public void initAllURI(String path) {
		// TODO Auto-generated method stub
		ServiceDiscovery.watchNode(zk);
	}
	
	public static ZkServerInitialization getInstance(String registryAddress){
		ZkServerInitialization.registryAddress=registryAddress;
		return new ZkServerInitialization();
		
	}
	
	public void colse(){
		try {
			if(zk!=null){
				zk.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(this.getClass().getName()+":{}",e);
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
	

}