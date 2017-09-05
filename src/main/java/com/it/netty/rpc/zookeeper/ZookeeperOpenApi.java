package com.it.netty.rpc.zookeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import com.it.netty.rpc.cluster.LoadBanlance;
import com.it.netty.rpc.cluster.RandomLoadBanlance;
import com.it.netty.rpc.message.URI;

public class ZookeeperOpenApi implements BeanFactoryAware {
		LoadBanlance banlance = new RandomLoadBanlance();
		protected static final Logger log = LoggerFactory.getLogger(ZookeeperOpenApi.class.getSimpleName());
		private final static String DEFAULT_ZOOKEEPER_NAME="default_client_zookeeper";
		private static ListableBeanFactory beanFactory;
		public  URI getURI(String className){
			RemoteAddress[] cache = ZookeeperService.cache_uri.getCache(className);
			return banlance.selectRandom(cache);
		}
		@Override
		public void setBeanFactory(BeanFactory beanFactory)
				throws BeansException {
			// TODO Auto-generated method stub
			ZookeeperOpenApi.beanFactory = (ListableBeanFactory) beanFactory;
		}
}		
