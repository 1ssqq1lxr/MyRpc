package com.it.netty.rpc.proxy.cglib;

import com.it.netty.rpc.filter.AbatractParameterFilter;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.proxy.ResolveProxy;

public class RpcCglibProxyClient extends ResolveProxy implements com.it.netty.rpc.proxy.Proxy{
	private String proxyName = "cglib";
	
	public String getProxyName() {
		return proxyName;
	}
	private final static CglibProxyMethodInterceptor cglibProxy = new CglibProxyMethodInterceptor();
	@SuppressWarnings("unchecked")
	public   <T>  T getProxy( Class<T> classes,AbatractParameterFilter<Invocation> filter) {
		return (T) cglibProxy.getProxy(classes, filter);
	}
}
