package com.it.netty.rpc.romote;

import io.netty.channel.ChannelFuture;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.message.URI;


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
		if(invocation==null || uri==null){
			throw new NullPointerException();
		}
		ChannelManager channelManager = channels.get(getRemoteStr(uri));
		if(channelManager==null){
			channelManager =doConnect(invocation.getUri()); // 连接服务器
			if(channelManager==null||!channelManager.isAvailable()){ // 可用的
				return null;
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
		// TODO Auto-generated method stub
		String UUID = java.util.UUID.randomUUID().toString();
		//1 设置到invocation 中
		Callback callback = new ResultCallBack(invocation.getTimeout());
		invocation.setSerialNo(UUID);
		callBacks.putIfAbsent(UUID, callback);
		// 1 设置UUID  2 设置超时时间
		return callback;
	}
	protected  void setCallBack(Result result) {
		// TODO Auto-generated method stub
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
