package com.it.netty.rpc.romote;

public abstract class AbstractNettyRemoteConnetion    {
    protected int port = 8099;

	public AbstractNettyRemoteConnetion(int port) {
		super();
		this.port = port;
	}
    
}
