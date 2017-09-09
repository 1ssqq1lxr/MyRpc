package com.it.netty.rpc.romote;

import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.cache.Cache;
import com.it.netty.rpc.cache.CacheFactory;
import com.it.netty.rpc.excutor.RpcExcutors;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Resolver;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.proxy.jdk.RpcJdkProxyClient;

/**
 * 
 * @author 17070680
 *
 */
public abstract class NettyServerApiService  {
	public static Cache<String,Integer>  proptitys = new CacheFactory<String, Integer>();
	protected static final Logger log = LoggerFactory.getLogger(NettyServerApiService.class.getSimpleName());
	protected final static RpcExcutors exRpcExcutors = new RpcExcutors("Rpc-method-server");
	protected void invoke(Channel channel,Invocation invocation){
		try {
			
			exRpcExcutors.excute(getSubmitTask(channel,invocation));
		} catch (Exception e) {
			log.error(this.getClass().getName()+"线程池执行失败 类{} 方法{} 参数{}", invocation.getClassName(),invocation.getMethodName(),invocation.getParams());
		}
	}
	public abstract Runnable getSubmitTask(Channel channel,Invocation invocation);
	


}
