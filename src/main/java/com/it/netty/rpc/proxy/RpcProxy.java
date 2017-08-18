package com.it.netty.rpc.proxy;
/**
 *  动态代理基类
 * @author 17070680
 *
 */
public interface RpcProxy{
	public abstract <T> T getProxy(Class<T> t);
}
