package com.it.netty.rpc.proxy.javassist;

import java.lang.reflect.Method;

import com.it.netty.rpc.filter.AbatractParameterFilter;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.proxy.RpcProxyService;
import com.it.netty.rpc.proxy.jdk.RpcJdkProxyClient;

import javassist.util.proxy.MethodHandler;

public class JavassistMethodHandler<T> extends RpcProxyService  implements MethodHandler{
	private Class<T> classes;
	private AbatractParameterFilter<Invocation> filter;
	public JavassistMethodHandler(Class<T> classes,AbatractParameterFilter<Invocation> filter) {
		this.classes = classes;
		this.filter=filter;
	}
	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed,
			Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		logger.info("success {} create proxy :{}",RpcJdkProxyClient.class.getName(),classes.getName());
		return doProxy(filter,thisMethod, classes, args);
	}
	
}
