package com.it.netty.rpc.protocol;

/**
 * 
 * @author 17070680
 *
 */
public interface ProtocolFactorySelector {
	   ProtocolFactory select(int protocol);
}
