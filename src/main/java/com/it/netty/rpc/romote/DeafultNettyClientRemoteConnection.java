package com.it.netty.rpc.romote;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class DeafultNettyClientRemoteConnection extends AbstractNettyRemoteConnetion {
	private DefaultEventLoopGroup ClientdefLoopGroup;
	private NioEventLoopGroup clientGroup;
	private ChannelFuture cf;
	private Bootstrap b;

	public DeafultNettyClientRemoteConnection(int port) {
		super(port);
		resource();
		// TODO Auto-generated constructor stub
	}
	
    public void resource(){
    	this.ClientdefLoopGroup = new DefaultEventLoopGroup(8, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "sxsDEFAULTEVENTLOOPGROUP_" + index.incrementAndGet());
            }
        });
    	this.clientGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "Client_" + index.incrementAndGet());
            }
        });
     
    	this.b = new Bootstrap();
    }
	
    

}
