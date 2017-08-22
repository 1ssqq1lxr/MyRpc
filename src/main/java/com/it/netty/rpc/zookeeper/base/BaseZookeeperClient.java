package com.it.netty.rpc.zookeeper.base;

import com.it.netty.rpc.message.URI;

/**
 * 基础客户端类
 * @author 17070680
 *
 */
public interface BaseZookeeperClient {
	void registURI(String path,URI uri);
	void initAllURI(String path);
}
