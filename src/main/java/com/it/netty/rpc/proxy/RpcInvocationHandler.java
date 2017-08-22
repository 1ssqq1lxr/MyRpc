package com.it.netty.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.it.netty.rpc.exception.NoFindClassException;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.romote.Callback;
import com.it.netty.rpc.romote.DeafultNettyClientRemoteConnection;
import com.it.netty.rpc.zookeeper.ServiceDiscovery;
/**
 * rpc客户端代码类
 * @author 17070680
 *
 * @param <T>
 */
public class RpcInvocationHandler<T> implements InvocationHandler{


	private static ConcurrentHashMap<Object, Invocation>  map = new ConcurrentHashMap<>();
	
	private DeafultNettyClientRemoteConnection connection = DeafultNettyClientRemoteConnection.newInstance();


	Class<T> classes;

	public RpcInvocationHandler(Class<T> classes) {
		super();
		this.classes = classes;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
	
		Invocation invocation = new Invocation();
		invocation.setClassName(classes.getName());
		invocation.setInterfaceClass(classes);
		invocation.setSerialNo(UUID.randomUUID().toString());
		invocation.setParams(args);
		invocation.setParamsType(method.getParameterTypes());
		invocation.setMethodName(method.getName());
		URI uri = ServiceDiscovery.findURIByPath(classes.getName());
		if(uri==null)
			throw new NoFindClassException();
		invocation.setUri(uri);
		Callback invokeAsync = connection.invokeAsync(invocation);
		return invokeAsync.getObject();
	}


}
