package com.it.netty.rpc.zookeeper;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.romote.NettyClientApiService;

public class ZookeeperOpenApi implements BeanFactoryAware {
		protected static final Logger log = LoggerFactory.getLogger(ZookeeperOpenApi.class.getSimpleName());
		private final static String DEFAULT_ZOOKEEPER_NAME="default_client_zookeeper";
		private static ListableBeanFactory beanFactory;
		public static URI getURI(String className){
			RemoteAddress[] cache = ZookeeperService.cache_uri.getCache(className);
//			if(cache==null){
//				ZookeeperService bean = (ZookeeperService) beanFactory.getBean(DEFAULT_ZOOKEEPER_NAME);
//				URI data = bean.getData(className);
//				if(data!=null){
//					ZookeeperService.cache_uri.addCache(className,data);
//					try {
//						bean.setListenter(className);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						log.error("初始化zookeeper节点{}监听器失败{}",className,e);
//					}
//					return data;
//				}
//			}
//			else{
//				Collection<URI> values = cache.values();
//				URI[] uris = new URI[values.size()];
//				int i=0;
//				for(URI uri:values){
//					uris[i]=uri;
//					i++;
//				}
//				return uris[0];
//			}
			return cache[0].getUri();
		}

		@Override
		public void setBeanFactory(BeanFactory beanFactory)
				throws BeansException {
			// TODO Auto-generated method stub
			ZookeeperOpenApi.beanFactory = (ListableBeanFactory) beanFactory;
		}
}		
