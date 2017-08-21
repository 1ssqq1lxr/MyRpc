package com.it.netty.rpc;

import com.it.netty.rpc.cache.Cache;
import com.it.netty.rpc.cache.CacheFactory;

import io.netty.channel.ChannelHandlerContext;

public class Config {
		public  static Cache<String,ChannelHandlerContext>  channelHandlerContexts = new CacheFactory<>(); // channel
}
