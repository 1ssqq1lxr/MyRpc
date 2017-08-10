package com.it.netty.rpc.core;

import io.netty.channel.ChannelFuture;

public interface Base {
	ChannelFuture start();
		void shutdown();
		void init();
}
