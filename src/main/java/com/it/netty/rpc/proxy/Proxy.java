package com.it.netty.rpc.proxy;

import com.it.netty.rpc.filter.AbatractParameterFilter;
import com.it.netty.rpc.message.Invocation;

public interface Proxy {
	public String getProxyName();
	public <T>  T getProxy( Class<T> classes,AbatractParameterFilter<Invocation> filter);
}
