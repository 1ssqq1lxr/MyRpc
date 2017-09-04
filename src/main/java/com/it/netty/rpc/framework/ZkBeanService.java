package com.it.netty.rpc.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.it.netty.rpc.zookeeper.ZookeeperService;


public class ZkBeanService implements InitializingBean{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
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
		logger.info(""+zookeeperService);
		zookeeperService.initRegist(registClassNames);
	}	
}
