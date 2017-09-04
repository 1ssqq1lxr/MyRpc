package com.it.netty.rpc.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.cache.Cache;
import com.it.netty.rpc.cache.CacheFactory;
import com.it.netty.rpc.proxy.cglib.RpcCglibProxyClient;
import com.it.netty.rpc.proxy.javassist.RpcJavassistProxyClient;
import com.it.netty.rpc.proxy.jdk.RpcJdkProxyClient;

public class RpcProxyFactorySelecter {
	private static final Logger log = LoggerFactory.getLogger(RpcProxyFactorySelecter.class.getSimpleName());
	public static Cache<String, Proxy> proxys = new CacheFactory<String, Proxy>();
	public RpcProxyFactorySelecter(){
		this.registProxy(new RpcCglibProxyClient());
		this.registProxy(new RpcJdkProxyClient());
		this.registProxy(new RpcJavassistProxyClient());
	}	
	final void registProxy(Proxy proxy){
		log.info("success registProxy {} into RpcProxyFactorySelecter ",proxy.getProxyName());
		proxys.putIfAbsentCache(proxy.getProxyName(), proxy);
	}
	public Proxy selectProxy(String proxyName){
		Proxy cache = proxys.getCache(proxyName);
		return cache;
	}
}
