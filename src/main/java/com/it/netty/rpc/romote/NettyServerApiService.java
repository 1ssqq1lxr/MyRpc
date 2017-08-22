package com.it.netty.rpc.romote;

import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.excutor.RpcExcutors;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Resolver;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.proxy.RpcProxyClient;


public abstract class NettyServerApiService  {
	protected static final Logger log = LoggerFactory.getLogger(NettyServerApiService.class.getSimpleName());

	
	protected final static RpcExcutors exRpcExcutors = new RpcExcutors("Rpc-method-server");
	
	
	
	protected void invoke(Channel channel,Invocation invocation){
//			if(invocation!=null){
//				Class<?> interfaceClass = invocation.getInterfaceClass();
//				Object newInstance = null;// 从spring 容器中 获取bean
//				Resolver resolver = RpcProxyClient.getInvocation(newInstance);
//				Result result = resolver.invoke(invocation);
//				result.setSerialNo(invocation.getSerialNo());
//				channel.writeAndFlush(result);
//			}
		try {
			exRpcExcutors.excute(getSubmitTask(channel,invocation));
		} catch (Exception e) {
			// TODO: handle exception
			log.error(this.getClass().getName()+"线程池执行失败 类{} 方法{} 参数{}", invocation.getClassName(),invocation.getMethodName(),invocation.getParams());
		}
	}
	public abstract Runnable getSubmitTask(Channel channel,Invocation invocation);
	


}
