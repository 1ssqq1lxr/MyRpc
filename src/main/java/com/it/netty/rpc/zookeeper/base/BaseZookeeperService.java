package com.it.netty.rpc.zookeeper.base;

import java.util.List;

import org.apache.zookeeper.CreateMode;

import com.it.netty.rpc.message.URI;

public interface BaseZookeeperService {
		public void registNode(String path,URI uri,CreateMode mode,boolean is);
		public void removeNode(String path,boolean is);
		public boolean exists(String path);
		public List<URI> getChildNodes(String path);
		public URI getData(String path);
}
