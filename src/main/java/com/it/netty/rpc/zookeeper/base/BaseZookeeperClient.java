package com.it.netty.rpc.zookeeper.base;

import org.apache.curator.framework.CuratorFramework;

import com.it.netty.rpc.zookeeper.Certificate;


/**
 * 基础客户端类
 * @author 17070680
 *
 */
public interface BaseZookeeperClient {
	CuratorFramework init(String path,String connectString,Certificate certificate);
}
