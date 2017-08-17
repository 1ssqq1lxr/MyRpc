package com.it.netty.rpc.core;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.InitializingBean;

import com.it.netty.rpc.handler.RpcServerHandler;

public class RpcServerInit extends AbstractBaseServer implements InitializingBean {

	private ScheduledExecutorService executorService;
	
	public RpcServerInit() {
		this.init();
        executorService = Executors.newScheduledThreadPool(2);
    }

    public RpcServerInit(int port) {
        this.port = port;
        executorService = Executors.newScheduledThreadPool(2);
    }

    public ChannelFuture start() {
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    final public static int MESSAGE_LENGTH = 4;
                    protected void initChannel(SocketChannel ch) throws Exception {
      				ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, this.MESSAGE_LENGTH, 0, this.MESSAGE_LENGTH));
       				ch.pipeline().addLast(new LengthFieldPrepender(this.MESSAGE_LENGTH));
       				ch.pipeline().addLast(new ObjectEncoder());
      				ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        				ch.pipeline().addLast(new  RpcServerHandler());
        		       	ch.pipeline().addLast(new IdleStateHandler(30,0 , 0));
                    }
                }) .childOption(ChannelOption.SO_KEEPALIVE, true);;

        try {
            cf = b.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) cf.channel().localAddress();
            logger.info("erver start success, port is:{}", addr.getPort());


        } catch (InterruptedException e) {
            logger.error("Server start fail,", e);
        }
		return cf;
    }

    @Override
    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
        }
        super.shutdown();
    }
    public static void main(String[] args) {
    	RpcServerInit init = new RpcServerInit();
    	init.start();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		this.start();
		logger.info(this.getClass().getName()+": tcp 服务启动成功");
	}
	
	
}
