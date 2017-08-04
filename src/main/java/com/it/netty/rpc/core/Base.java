package com.it.netty.rpc.core;

import io.netty.channel.ChannelFuture;

public interface Base {
		//开启服务
	ChannelFuture start();
		void shutdown();
		void init();
}
