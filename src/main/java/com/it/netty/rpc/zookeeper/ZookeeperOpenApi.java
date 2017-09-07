package com.it.netty.rpc.zookeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import com.it.netty.rpc.cluster.LoadBanlance;
import com.it.netty.rpc.message.URI;

public class ZookeeperOpenApi {
		protected static final Logger log = LoggerFactory.getLogger(ZookeeperOpenApi.class.getSimpleName());
		public  URI getURI(LoadBanlance loadBanlance,String className){
			RemoteAddress[] cache = ZookeeperService.cache_uri.getCache(className);
			if(cache==null || cache.length<1)
				return null;
			else if(cache.length ==1)
				return cache[0].getUri();
			else
				return loadBanlance.selectRandom(cache);
		}
}		
