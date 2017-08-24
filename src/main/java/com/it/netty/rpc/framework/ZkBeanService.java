package com.it.netty.rpc.framework;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.it.netty.rpc.zookeeper.ZookeeperService;


public class ZkBeanService implements InitializingBean{
	
	
	private  ConcurrentHashSet<String> registClassNames  = new ConcurrentHashSet<>();
	
	private ZookeeperService zookeeperService;
	
	
	
	public ZookeeperService getZookeeperService() {
		return zookeeperService;
	}

	public void setZookeeperService(ZookeeperService zookeeperService) {
		this.zookeeperService = zookeeperService;
	}

	public ConcurrentHashSet<String> getRegistClassNames() {
		return registClassNames;
	}

	public void setRegistClassNames(ConcurrentHashSet<String> registClassNames) {
		this.registClassNames = registClassNames;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		zookeeperService.initRegist(registClassNames);
	}	
}
