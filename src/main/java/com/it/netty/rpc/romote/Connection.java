package com.it.netty.rpc.romote;

import io.netty.channel.ChannelFuture;

/**
 * 
 * @author 17070680
 *
 */
public interface Connection {
	public ChannelFuture init();
}
