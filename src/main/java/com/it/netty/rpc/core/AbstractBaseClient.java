package com.it.netty.rpc.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 基础客户端抽象类
 * @author 17070680
 *
 */
public abstract class AbstractBaseClient implements Base{
	abstract ChannelFuture connect();
	protected Logger logger = LoggerFactory.getLogger(getClass());
    protected String host = "localhost";
    protected int port = 8099;

    protected DefaultEventLoopGroup ClientdefLoopGroup;
    protected NioEventLoopGroup clientGroup;
    protected ChannelFuture cf;
    protected Bootstrap b;

    public void init(){
    	ClientdefLoopGroup = new DefaultEventLoopGroup(8, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "sxsDEFAULTEVENTLOOPGROUP_" + index.incrementAndGet());
            }
        });
        clientGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "Client_" + index.incrementAndGet());
            }
        });
     

        b = new Bootstrap();
    }

    public void shutdown() {
        if (ClientdefLoopGroup != null) {
        	ClientdefLoopGroup.shutdownGracefully();
        }
        clientGroup.shutdownGracefully();
    }
}
