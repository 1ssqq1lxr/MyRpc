package com.it.netty.rpc.proxy;

import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import com.it.netty.rpc.core.RpcClientInit;
import com.it.netty.rpc.core.RpcLoader;
import com.it.netty.rpc.handler.RpcClientHandler;
import com.it.netty.rpc.message.MsgBackCall;
import com.it.netty.rpc.message.MsgRequest;
import com.it.netty.rpc.zookeeper.ServiceDiscovery;
/**
 * rpc客户端代码类
 * @author 17070680
 *
 * @param <T>
 */
public class RpcInvocationHandler<T> implements InvocationHandler{
	Class<T> classes;
	
	public RpcInvocationHandler(Class<T> classes) {
		super();
		this.classes = classes;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		RpcLoader getloader = RpcLoader.getloader();
		if(getloader!=null){
			ServiceDiscovery.threadLocal.set(classes.getName());
			// TODO Auto-generated method stub
			MsgRequest msgRequest= new MsgRequest();
			msgRequest.setSiralNo(UUID.randomUUID().toString());
			System.out.println();
			msgRequest.setClassName(classes.getName());
			msgRequest.setMethodName(method.getName());
			Class<?>[] parameterTypes = method.getParameterTypes();
			msgRequest.setParamsType(parameterTypes);
			msgRequest.setParams(args);
			msgRequest.setReturnType( method.getReturnType());
			MsgBackCall sendMag = RpcClientHandler.sendMag(msgRequest);
			if(msgRequest.getReturnType().getSimpleName().equals("void")) // 返回值 为void 直接返回不等待
			return null;
			else
			return sendMag.call();  // 回调
		}
		return null;
	}


}
