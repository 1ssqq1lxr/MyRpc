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

public class RpcClientInit extends AbstractBaseClient{
	
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
			        //利用LengthFieldPrepender回填补充ObjectDecoder消息报文头
				ch.pipeline().addLast(new LengthFieldPrepender(this.MESSAGE_LENGTH));
				ch.pipeline().addLast(new ObjectEncoder());
			        //考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
				ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
//				
//				ch.pipeline().addLast(new ObjectEncoder());
//				ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.softCachingConcurrentResolver(this.getClass().getClassLoader())));
				ch.pipeline().addLast(new  RpcClientHandler(RpcClientInit.this));
				ch.pipeline().addLast(new IdleStateHandler(20, 0, 0));
				//检测链路是否读空闲    	
			}
		});
		return this.connect();
	}

	@Override
	public  ChannelFuture connect() {
		ChannelFuture sync = b.connect("127.0.0.1",8099);
		sync.addListener(new ChannelFutureListener(){

			public void operationComplete(ChannelFuture future)
					throws Exception {
				// TODO Auto-generated method stub
				if (future.isSuccess()) {
					Channel  channel = future.channel();
					System.out.println("Connect to server successfully!"+channel.toString());
				} else {
					System.out.println("Failed to connect to server, try connect after 10s");
					executorService.execute(new ChannelConnect(RpcClientInit.this));
				}
			}
		});
		return sync;
	}
	public static void main(String[] args) {
		RpcClientInit init = new RpcClientInit();
		init.start();
	}
}
