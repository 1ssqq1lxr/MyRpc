package com.it.netty.rpc.zookeeper.base;

import org.apache.zookeeper.CreateMode;

import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.zookeeper.RemoteAddress;

public interface BaseZookeeperService {
		public void registNode(String path,URI uri,CreateMode mode,boolean is);
		public void removeNode(String path,boolean is);
		public boolean exists(String path);
		public RemoteAddress[] getChildNodes(String path);
		public URI getData(String path);
}
