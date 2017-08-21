package com.it.netty.rpc.romote;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.it.netty.rpc.exception.ClientConnectionException;
import com.it.netty.rpc.message.Const;
import com.it.netty.rpc.message.URI;

public class DeafultNettyClientRemoteConnection  extends NettyApiService{
	private  DefaultEventLoopGroup ClientdefLoopGroup;
	private NioEventLoopGroup clientGroup;
	private ChannelFuture cf;
	private Bootstrap b;
	private final Lock lock = new ReentrantLock();
	static class staticInitBean{
		public static DeafultNettyClientRemoteConnection clientRemoteConnection = new DeafultNettyClientRemoteConnection();
	}
	public static DeafultNettyClientRemoteConnection newInstance(){
		return staticInitBean.clientRemoteConnection;
	}
	private  DeafultNettyClientRemoteConnection() {
		//		super(port);
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
	public void start() {
		b.group(clientGroup)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_KEEPALIVE, false)
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
		.option(ChannelOption.SO_SNDBUF, 65535)
		.option(ChannelOption.SO_RCVBUF, 65535)
		.handler(new ChannelInitializer<SocketChannel>() {
			final public static int MESSAGE_LENGTH = 4;
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
						
			}
		});
	}
	@Override
	public ChannelManager doConnect(final URI uri) {
			
		// TODO Auto-generated method stub
			try {
				if(this.lock.tryLock(Const.TIME_OUT, TimeUnit.MILLISECONDS)){
					
					ChannelManager channelManager = DeafultNettyClientRemoteConnection.channels.get(getRemoteStr(uri));
					if(channelManager==null){
						final ChannelFuture connect = b.connect(uri.getHost(), uri.getPort());
						if(connect.isSuccess()){
							ChannelManager channelManager1 = new ChannelManager(connect);
							DeafultNettyClientRemoteConnection.channels.putIfAbsent(getRemoteStr(uri), channelManager1);
							return channelManager;
						}
						log.error(this.getClass().getName()+" 连接｛｝ 失败",getRemoteStr(uri));
					}
					else
						return channelManager;
					
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error(this.getClass().getName(), e);
			}
			return null;
	
		
	}
	
	
	
}
