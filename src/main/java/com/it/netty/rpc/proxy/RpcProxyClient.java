package com.it.netty.rpc.proxy;

import java.lang.reflect.Proxy;
/**
 * 动态代理客户端
 * @author 17070680
 *
 * @param <T>
 */
public class RpcProxyClient {


	public  static <T>  T getProxy( Class<T> classes) {
		// TODO Auto-generated method stub
		return (T) Proxy.newProxyInstance(classes.getClassLoader(), new Class<?>[]{classes}, new RpcInvocationHandler<T>(classes));
	}

}
