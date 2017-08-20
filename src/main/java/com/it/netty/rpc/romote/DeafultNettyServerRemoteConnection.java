package com.it.netty.rpc.romote;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DeafultNettyServerRemoteConnection extends AbstractNettyRemoteConnetion {
	private DefaultEventLoopGroup ServerdefLoopGroup;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workGroup;
	private ChannelFuture cf;
	private ServerBootstrap b;

	public void resouce(){
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
	public DeafultNettyServerRemoteConnection(int port) {
		super(port);
		resouce();
		// TODO Auto-generated constructor stub
	}

}
