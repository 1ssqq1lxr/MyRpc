package com.it.netty.rpc.service;

import java.lang.annotation.Annotation;
import java.util.Map;

import javassist.NotFoundException;

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
	/*
	 * <p>Title: getObject</p>
	 * <p>Description: </p>
	 * @param className
	 * @return
	 * @throws Exception
	 * @see com.it.netty.rpc.service.ServiceObjectFindInteferce#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String className) throws Exception {
		// TODO Auto-generated method stub
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
				  serviceInterface = this.classLoader.loadClass(className);
				  if(serviceInterface!=null){
					  Map<String, ?> beans = beanFactory.getBeansOfType(serviceInterface);
					  for (Map.Entry<String, ?> e : beans.entrySet()) {
			                Object bean = e.getValue();
			                object.putIfAbsentCache(className, bean);
			                return bean;
//			                Class<?> beanType = bean.getClass();
//			                RpcService annotation = bean.getClass().getAnnotation(RpcService.class);
//			                Annotation[] annotations = beanType.getAnnotations();
//			                
//			                
//			                if (beanType.isAnnotationPresent(RpcService.class)) {
//			                	return bean;
//			                }
			            }
			           throw new NotFoundException(className);
				  }
			} catch (Exception e) {
				// TODO: handle exception
				log.error("bean class " + className + " is not exists.", e);
	            throw e;
			}
		}
		return null;
	}
			
}
