package com.it.netty.rpc.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import com.it.netty.rpc.annocation.RpcService;
import com.it.netty.rpc.cache.Cache;
import com.it.netty.rpc.cache.CacheFactory;
import com.it.netty.rpc.exception.NoFindClassException;
import com.it.netty.rpc.exception.NotFoundServiceException;
import com.it.netty.rpc.romote.NettyClientApiService;

public class ServiceObjectFind implements ServiceObjectFindInteferce, BeanFactoryAware, BeanClassLoaderAware{
	protected  Logger log = LoggerFactory.getLogger(NettyClientApiService.class.getSimpleName());
	private ListableBeanFactory beanFactory;
    private ClassLoader classLoader;
    private static Cache<String, Object> object  = new CacheFactory<>();
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
	@Override
	public Object getObject(String className) throws RuntimeException {
		if(className==null){
			return null;
		}
		Object cache = object.getCache(className);
		if(cache!=null){
			return cache;
		}
		else{
			Class<?> serviceInterface = null;
			try {
				  try {
					serviceInterface = this.classLoader.loadClass(className);
					 if(serviceInterface!=null){
						  Map<String, ?> beans = beanFactory.getBeansOfType(serviceInterface);
						  if(!beans.isEmpty()){
							  for (Map.Entry<String, ?> e : beans.entrySet()) {
					                Object bean = e.getValue();
					                object.putIfAbsentCache(className, bean);
					                Class<?> beanType = bean.getClass();
					                if (beanType.isAnnotationPresent(RpcService.class)) {
					                	return bean;
					                }
					            }
						  }
				           throw new NotFoundServiceException(className);
					  }
				} catch (ClassNotFoundException e1) {
					throw new NoFindClassException(className);
				}
				
			} catch (RuntimeException e) {
				throw e;
			}
		}
		return null;
	}			
}
