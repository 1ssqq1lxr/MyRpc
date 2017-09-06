package com.it.netty.rpc.proxy.jdk;


import java.lang.reflect.Proxy;

import com.it.netty.rpc.filter.AbatractParameterFilter;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.proxy.ResolveProxy;
/**
 * 动态代理客户端
 * @author 17070680
 *
 * @param <T>
 */
public class RpcJdkProxyClient  extends ResolveProxy implements com.it.netty.rpc.proxy.Proxy{
	private String proxyName = "JDK";
	
	public String getProxyName() {
		return proxyName;
	}
	@SuppressWarnings("unchecked")
	public   <T>  T getProxy( Class<T> classes,AbatractParameterFilter<Invocation> filter) {
		T newProxyInstance = (T) Proxy.newProxyInstance(classes.getClassLoader(), new Class<?>[]{classes}, new RpcInvocationHandler<T>(classes,filter));
		return (T) newProxyInstance;
	}
	
}
