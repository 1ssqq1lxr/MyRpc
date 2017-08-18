package com.it.netty.rpc.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.it.netty.rpc.handler.RpcClientHandler;
import com.it.netty.rpc.heart.ChannelConnect;
import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.zookeeper.ServiceDiscovery;

public class RpcClientInit extends AbstractBaseClient{
	public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
	private Channel channel;
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	private ScheduledExecutorService executorService;
	public RpcClientInit() {
		this.init();
		executorService = Executors.newScheduledThreadPool(2);
	}

	public RpcClientInit(int port) {
		this.init();
		this.port = port;
		executorService = Executors.newScheduledThreadPool(2);
	}
	public ChannelFuture start() {
		// TODO Auto-generated method stub
		b.group(clientGroup)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.handler(new ChannelInitializer<SocketChannel>() {
			final public static int MESSAGE_LENGTH = 4;
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
			
				ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, this.MESSAGE_LENGTH, 0, this.MESSAGE_LENGTH));
				ch.pipeline().addLast(new LengthFieldPrepender(this.MESSAGE_LENGTH));
				ch.pipeline().addLast(new ObjectEncoder());
				ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
				ch.pipeline().addLast(new  RpcClientHandler(RpcClientInit.this));
				ch.pipeline().addLast(new IdleStateHandler(20, 0, 0));
			}
		});
		return this.connect();
	}

	@Override
	public  ChannelFuture connect() {
		try {
			URI findURIByThread = ServiceDiscovery.findURIByThread();
			if(findURIByThread==null)
				throw new RuntimeException("没找到匹配的service {}");
			ChannelFuture sync = b.connect(findURIByThread.getHost(),findURIByThread.getPort());
			sync.addListener(new ChannelFutureListener(){

				public void operationComplete(ChannelFuture future)
						throws Exception {
					// TODO Auto-generated method stub
					if (future.isSuccess()) {
						Channel  channel = future.channel();
						logger.info("Connect to server successfully!"+channel.toString());
					} else {
						logger.info("Failed to connect to server, try connect after 20s");
						executorService.execute(new ChannelConnect(RpcClientInit.this));
					}
				}
			});
			return sync;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(this.getClass().getName()+": service {} not finded" ,ServiceDiscovery.threadLocal.get());
		}
		return cf;
		
	}
	
}
