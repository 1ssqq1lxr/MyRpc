package com.it.netty.rpc.proxy.javassist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.util.proxy.ProxyFactory;

import com.it.netty.rpc.filter.AbatractParameterFilter;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.proxy.ResolveProxy;

public class RpcJavassistProxyClient  extends ResolveProxy implements com.it.netty.rpc.proxy.Proxy{
	private String proxyName = "javassist";
	public String getProxyName() {
		return proxyName;
	}
	@SuppressWarnings("unchecked")
	public   <T>  T getProxy( Class<T> classes,AbatractParameterFilter<Invocation> filter) {
	     try {
	    	 ProxyFactory proxyFactory = new ProxyFactory();
	 	     proxyFactory.setSuperclass(classes);
	 	     proxyFactory.setHandler(new JavassistMethodHandler<T>(classes, filter));
	 	     return (T) proxyFactory.createClass().newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("error create RpcJavassistProxy ",e);
		}
		return null;
	}
}
