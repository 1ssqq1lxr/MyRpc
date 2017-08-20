package com.it.netty.rpc;

import com.it.netty.rpc.cache.Cache;
import com.it.netty.rpc.cache.CacheFactory;
import com.it.netty.rpc.message.Timeout;

import io.netty.channel.ChannelHandlerContext;

public class Config {
		public  static Cache<String,ChannelHandlerContext>  channelHandlerContexts = new CacheFactory<>(); // channel
		public  static Cache<String,Timeout>  timeouts = new CacheFactory<>(); // 超时
}
