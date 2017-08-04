package com.it.netty.rpc.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 基础服务端抽象类
 * @author 17070680
 *
 */
public abstract class AbstractBaseServer implements Base {

	  	protected Logger logger = LoggerFactory.getLogger(getClass());
	    protected String host = "localhost";
	    protected int port = 8099;

	    protected DefaultEventLoopGroup ServerdefLoopGroup;
	    protected NioEventLoopGroup bossGroup;
	    protected NioEventLoopGroup workGroup;
	    protected ChannelFuture cf;
	    protected ServerBootstrap b;

	    public void init(){
	    	ServerdefLoopGroup = new DefaultEventLoopGroup(8, new ThreadFactory() {
	            private AtomicInteger index = new AtomicInteger(0);

	            public Thread newThread(Runnable r) {
	                return new Thread(r, "DEFAULTEVENTLOOPGROUP_" + index.incrementAndGet());
	            }
	        });
	        bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
	            private AtomicInteger index = new AtomicInteger(0);

	            public Thread newThread(Runnable r) {
	                return new Thread(r, "BOSS_" + index.incrementAndGet());
	            }
	        });
	        workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 10, new ThreadFactory() {
	            private AtomicInteger index = new AtomicInteger(0);

	            public Thread newThread(Runnable r) {
	                return new Thread(r, "WORK_" + index.incrementAndGet());
	            }
	        });

	        b = new ServerBootstrap();
	    }

	    public void shutdown() {
	        if (ServerdefLoopGroup != null) {
	        	ServerdefLoopGroup.shutdownGracefully();
	        }
	        bossGroup.shutdownGracefully();
	        workGroup.shutdownGracefully();
	    }
}
