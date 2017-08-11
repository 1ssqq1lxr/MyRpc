package com.it.netty.rpc.proxy;

import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import com.it.netty.rpc.core.RpcLoader;
import com.it.netty.rpc.handler.RpcClientHandler;
import com.it.netty.rpc.message.MsgBackCall;
import com.it.netty.rpc.message.MsgRequest;
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
			// TODO Auto-generated method stub
			MsgRequest msgRequest= new MsgRequest();
			msgRequest.setSiralNo(UUID.randomUUID().toString());
			System.out.println();
			msgRequest.setClassName(classes.getName());
			msgRequest.setMethodName(method.getName());
			msgRequest.setParams(args);
			MsgBackCall sendMag = RpcClientHandler.sendMag(msgRequest);
			return sendMag.call(); // 回调
		}
		return null;
	}


}
