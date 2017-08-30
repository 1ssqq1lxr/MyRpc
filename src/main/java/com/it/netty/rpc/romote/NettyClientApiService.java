package com.it.netty.rpc.romote;

import io.netty.channel.ChannelFuture;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.exception.ClientConnectionException;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.message.URI;

/**
 * 
 * @author 17070680
 *
 */
public abstract class NettyClientApiService {
	protected static final Logger log = LoggerFactory.getLogger(NettyClientApiService.class.getSimpleName());

	protected static final ConcurrentHashMap<String, ChannelManager> channels = new ConcurrentHashMap<>();

	protected static  final ConcurrentHashMap<String, Callback> callBacks= new ConcurrentHashMap<String, Callback>();

	public static String getRemoteStr(URI uri){
		if(uri!=null){
			return uri.getHost()+":"+uri.getPort();
		}
		return null;
	}
	public   Callback invokeAsync(Invocation invocation){
		URI uri = invocation.getUri();
		String remoteStr = getRemoteStr(uri);
		if(invocation==null || uri==null){
			throw new NullPointerException(invocation.toString());
		}
		ChannelManager channelManager = channels.get(remoteStr);
		if(channelManager==null){
			channelManager =doConnect(invocation.getUri()); // 连接服务器
			if(channelManager==null||!channelManager.isAvailable()){ // 可用的
					throw new ClientConnectionException(remoteStr);
			}
		}
		ChannelFuture channelFuture = channelManager.getChannelFuture();
		Callback sendMessage = sendMessage(channelFuture,invocation);
		return sendMessage;
	}
	public abstract ChannelManager doConnect(URI uri);
	
	protected   Callback sendMessage(ChannelFuture channelFuture,Invocation invocation){
		Callback initCallBack = initCallBack(invocation);
		channelFuture.channel().writeAndFlush(invocation);
		return initCallBack;
	};
	
	protected  Callback initCallBack(Invocation invocation) {
		String UUID = java.util.UUID.randomUUID().toString();
		Callback callback = new ResultCallBack(invocation.getTimeout(),invocation.getClassName()+":"+invocation.getMethodName());
		invocation.setSerialNo(UUID);
		callBacks.putIfAbsent(UUID, callback);
		return callback;
	}
	protected  void setCallBack(Result result) {
		if(result!=null){
			String serialNo = result.getSerialNo();
			Callback callback = callBacks.get(serialNo);
			if(callback!=null){
				log.info(callback.getClass().getName()+"找到对应的 callback{}", result);
				callback.putResult(result);
				callBacks.remove(serialNo);
			}
			else
			log.error(this.getClass().getName()+"未找到对应的 callback{}", result);
		}
	}



}
