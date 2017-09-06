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
		String methodName = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (method.getDeclaringClass() == Object.class) {
			return doProxy(filter, method, args);
		}
		if ("toString".equals(methodName) && parameterTypes.length == 0) {
			return proxy.toString();
		}
		if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
			return proxy.hashCode();
		}
		if ("equals".equals(methodName) && parameterTypes.length == 1) {
			return proxy.equals(args[0]);
		}
		return doProxy(filter, method, args);
	}  
}
