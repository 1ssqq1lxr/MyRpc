package com.it.netty.rpc.romote;

import io.netty.channel.ChannelFuture;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.URI;


public abstract class NettyApiService {
	private static final Logger log = LoggerFactory.getLogger(NettyApiService.class.getSimpleName());

	private static ConcurrentHashMap<String, ChannelFuture> channels = new ConcurrentHashMap<>();
	
	private static  ConcurrentHashMap<String, Callback> callBacks= new ConcurrentHashMap<String, Callback>();
	
	public static String getRemoteStr(URI uri){
		if(uri!=null){
			return uri.getHost()+":"+uri.getPort();
		}
		return null;
	}
	public   Callback invokeAsync(URI uri,Invocation invocation){
		if(uri!=null){
			ChannelFuture channelFuture = channels.get(getRemoteStr(uri));
			if(channelFuture!=null){
				Callback callback=	initCallBack(invocation);
				channelFuture.channel().writeAndFlush(invocation);
				return callback;
			}
		}
		return null;
	}
	public  Callback initCallBack(Invocation invocation) {
		// TODO Auto-generated method stub
		String UUID = java.util.UUID.randomUUID().toString();
		//1 设置到invocation 中
		Callback callback = new ResultCallBack(3000);
		callBacks.putIfAbsent(UUID, callback);
		// 1 设置UUID  2 设置超时时间
		return callback;
	}
	public  void setCallBack(Invocation invocation) {
		// TODO Auto-generated method stub
		String UUID = java.util.UUID.randomUUID().toString();
		//1 设置到invocation 中
		Callback callback = new ResultCallBack(3000);
		callBacks.putIfAbsent(UUID, callback);
		// 1 设置UUID  2 设置超时时间
//		return callback;
	}
	
	

}
