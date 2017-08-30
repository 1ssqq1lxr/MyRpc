package com.it.netty.rpc.framework;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.it.netty.rpc.Config;
import com.it.netty.rpc.zookeeper.ZookeeperService;


public class ZkBeanServiceConsume implements InitializingBean{
	private  ConcurrentHashSet<String> getClassNames  = new ConcurrentHashSet<>();
	
	private String protocol;
	
	
	private String zkAddress;
	
	
	
	public String getZkAddress() {
		return zkAddress;
	}

	public void setZkAddress(String zkAddress) {
		this.zkAddress = zkAddress;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	private ZookeeperService zookeeperService;
	
	
	public ConcurrentHashSet<String> getGetClassNames() {
		return getClassNames;
	}

	public void setGetClassNames(ConcurrentHashSet<String> getClassNames) {
		this.getClassNames = getClassNames;
	}

	public ZookeeperService getZookeeperService() {
		return zookeeperService;
	}

	public void setZookeeperService(ZookeeperService zookeeperService) {
		this.zookeeperService = zookeeperService;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		Config.protocol=this.protocol;
		zookeeperService.initServer(getClassNames);;
	}
}
