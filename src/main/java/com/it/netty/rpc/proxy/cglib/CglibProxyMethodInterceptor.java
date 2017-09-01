package com.it.netty.rpc.proxy.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.filter.AbatractParameterFilter;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.proxy.RpcProxyService;

public class CglibProxyMethodInterceptor extends RpcProxyService implements MethodInterceptor{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	 private Enhancer enhancer = new Enhancer();  
		private Class classes;
		private AbatractParameterFilter<Invocation> filter;
	    public Object getProxy(Class clazz,AbatractParameterFilter<Invocation> filter) { 
	    	this.classes=clazz;
	    	this.filter=filter;
	        enhancer.setSuperclass(clazz);  
	        enhancer.setCallback(this);
	        logger.info("success {} create proxy :{}",RpcCglibProxyClient.class.getName(),classes.getName());
	        return enhancer.create();  
	    }  
	    @Override  
	    public Object intercept(Object obj, Method method, Object[] args,  
	            MethodProxy proxy) throws Throwable { 
	    		return doProxy(filter, method, classes, args);
	    }  
}
