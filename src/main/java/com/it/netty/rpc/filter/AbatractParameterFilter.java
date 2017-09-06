package com.it.netty.rpc.filter;

import java.lang.reflect.Method;

import com.it.netty.rpc.proxy.Proxy;


public interface  AbatractParameterFilter<T>  {
	public abstract  T doParameter(Method method,Object[] objects);
	public Proxy getDefaultProxy();
}
