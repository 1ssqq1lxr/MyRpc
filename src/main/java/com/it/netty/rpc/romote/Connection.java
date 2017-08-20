package com.it.netty.rpc.romote;

import io.netty.channel.ChannelFuture;


public interface Connection {
	public ChannelFuture init();
}
