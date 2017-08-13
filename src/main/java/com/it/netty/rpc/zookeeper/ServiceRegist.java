package com.it.netty.rpc.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.it.netty.rpc.zookeeper.base.URI;

public class ServiceRegist {
	protected static Logger logger = LoggerFactory.getLogger(ServiceRegist.class);

	    //注册到zk中，其中data为服务端的 ip:port
	    public static void register(ZooKeeper zk, String path, URI uri) {
	    	createNode(zk,path,getURIToBytes(uri));
	    }
	   
	    
	    private static void createNode(ZooKeeper zk, String path, byte[] bs) {
	        try {
	            String path1 = zk.create("/"+path, bs, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	            logger.info(ServiceRegist.class.getName()+":{} success regist zookeeper {}",path,path1);
	        } catch (Exception e) {
	         e.printStackTrace();
	         logger.error(ServiceRegist.class.getName()+":{} error regist zookeeper",path);
	        }
	    }
	    
	    public static byte[] getURIToBytes(URI uri){
	    	return JSON.toJSONString(uri).getBytes();
	    }


}
