package com.it.netty.rpc.romote;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.InitializingBean;

import com.it.netty.rpc.heart.HeartBeat;
import com.it.netty.rpc.message.Const;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Resolver;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.protocol.DefaultProtocolFactorySelector;
import com.it.netty.rpc.protocol.ProtocolFactory;
import com.it.netty.rpc.protocol.ProtocolFactorySelector;
import com.it.netty.rpc.protocol.SerializeEnum;
import com.it.netty.rpc.proxy.RpcProxyClient;
import com.it.netty.rpc.service.ServiceObjectFind;
import com.it.netty.rpc.service.ServiceObjectFindInteferce;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
/**
 * 
 * @author 17070680
 *
 */
public class DeafultNettyServerRemoteConnection extends NettyServerApiService implements InitializingBean {
	
	ServiceObjectFindInteferce serviceObjectFindInteferce ;
	
	
	
	
	public ServiceObjectFindInteferce getServiceObjectFindInteferce() {
		return serviceObjectFindInteferce;
	}

	public void setServiceObjectFindInteferce(
			ServiceObjectFindInteferce serviceObjectFindInteferce) {
		this.serviceObjectFindInteferce = serviceObjectFindInteferce;
	}
	private DefaultEventLoopGroup ServerdefLoopGroup;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workGroup;
	private  ServerBootstrap b;
	private int port;
	private ChannelFuture sync;
	private  final  ProtocolFactorySelector protocolFactorySelector = new DefaultProtocolFactorySelector();
	private final int TOP_LENGTH=129>>1|34; // 数据协议头
	private final int TOP_HEARTBEAT=129>>1|36; // 心跳协议头
	
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

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

	public void start() {
		b.group(bossGroup, workGroup)
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_KEEPALIVE, true)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_BACKLOG, 1024)
		.option(ChannelOption.SO_SNDBUF, 65535)
		.option(ChannelOption.SO_RCVBUF, 65535)
		.childHandler(new ChannelInitializer<SocketChannel>() {
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(ServerdefLoopGroup);
				ch.pipeline().addLast(new ServerEncode());
				ch.pipeline().addLast(new ServerDecode());
				ch.pipeline().addLast(new IdleStateHandler(30,30 ,30));
				ch.pipeline().addLast(new OutChannelInvocationHandler());
			}
		}) .childOption(ChannelOption.SO_KEEPALIVE, true);;
		try {
			sync = b.bind(port).sync();
			log.info(this.getClass().getName()+"启动成功 ：{}",sync.channel());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(this.getClass().getName()+"启动失败 ：{}",e);
		}
	}

	public DeafultNettyServerRemoteConnection() {
		resouce();
		// TODO Auto-generated constructor stub
	}
	
	class OutChannelInvocationHandler extends SimpleChannelInboundHandler<Object>{
		private AtomicInteger atomicInteger= new AtomicInteger(0);
		@Override
		protected void messageReceived(ChannelHandlerContext ctx, Object result)
				throws Exception {
			// TODO Auto-generated method stub
			if(result instanceof Invocation){
				invoke(ctx.channel(), (Invocation)result);
			}
			else if(result instanceof HeartBeat){
				atomicInteger.getAndAdd(0);// 重置心跳阙值
				log.info(this.getClass().getName()+"收到来自 {}的心跳消息{}", ctx.channel().remoteAddress(),result);
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
			// TODO Auto-generated method stub
			ctx.close();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			// TODO Auto-generated method stub
			super.channelActive(ctx);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			// TODO Auto-generated method stub
			Channel channel = ctx.channel();
			Set<String> keySet = DeafultNettyClientRemoteConnection.channels.keySet();
			for(String set:keySet){
				ChannelManager channelManager = DeafultNettyClientRemoteConnection.channels.get(set);
				if(channelManager.getChannel()==channel){
					DeafultNettyClientRemoteConnection.channels.remove(set);
				}
			}
			ctx.close();
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
				throws Exception {
			// TODO Auto-generated method stub
	
			IdleStateEvent e = (IdleStateEvent) evt; 
			if(atomicInteger.get()<6){
				if(e.state().equals(IdleState.READER_IDLE) ){
					log.info(this.getClass().getName()+"未收到来自{}的心跳次数{}", ctx.channel(),atomicInteger.incrementAndGet());
					
				}
			}
			else{
				log.info(this.getClass().getName()+"未收到来自{}的心跳次数 6 关闭连接",ctx.channel());
				ctx.close();
			}
			super.userEventTriggered(ctx, evt);
		}
		
		
		
	}
	
	class ServerEncode extends MessageToByteEncoder<Object> {

		protected void encode(ChannelHandlerContext ctx, Object invocation, ByteBuf out)
				throws Exception {
			if(invocation instanceof Result){ // 请求
				Result result =(Result)invocation;
				ProtocolFactory protocolFactory = getProtocolFactory(result);
				out.writeInt(TOP_LENGTH);
				byte[] encode = protocolFactory.encode(invocation);
				out.writeInt(protocolFactory.getProtocol());
				out.writeInt(encode.length);
				out.writeBytes(encode);
			}
			
		}
		protected ProtocolFactory getProtocolFactory(Result result){
			SerializeEnum valueOf = SerializeEnum.valueOf(result.getProtocol());
			valueOf=valueOf==null?SerializeEnum.DEFAULTSERIALIZE:valueOf;
			int serialNo = valueOf.getValue();
			ProtocolFactory protocolFactory = protocolFactorySelector.select(serialNo);
			return protocolFactory;
		}



	}
	class ServerDecode extends ByteToMessageDecoder{

		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf in,
				List<Object> out) throws Exception {
			int readIndex = in.readerIndex();
			if(in.readableBytes()<4){
				return;
			}
			int readInt = in.readInt();
			if(readInt!=TOP_LENGTH && readInt!=TOP_HEARTBEAT){
				log.warn(this.getClass().getName()+"消息协议头非法{}", readInt);
				return;
			}
			if(readInt==TOP_LENGTH){ // 处理消息
				int protocol = in.readInt();
				int bodylength = in.readInt();
				if(bodylength>in.readableBytes()){
					in.readerIndex(readIndex); //初始化读取位置
					return;
				}
				byte[] bytes = new byte[bodylength];
				ProtocolFactory select = protocolFactorySelector.select(protocol);
				in.readBytes(bytes);
				Invocation invocation = select.decode(Invocation.class, bytes);
				out.add(invocation);
			}
			else if(readInt==TOP_HEARTBEAT){ // 心跳
				int protocol = in.readInt();
				int bodylength = in.readInt();
				if(bodylength>in.readableBytes()){
					in.readerIndex(readIndex); //初始化读取位置
					return;
				}
				byte[] bytes = new byte[bodylength];
				ProtocolFactory select = protocolFactorySelector.select(protocol);
				in.readBytes(bytes);
				HeartBeat heartBeat = select.decode(HeartBeat.class, bytes);
				out.add(heartBeat);
			}
			
		}
	}
	@Override
	public Runnable getSubmitTask(final Channel channel,final Invocation invocation) {
		// TODO Auto-generated method stub
		return new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(invocation!=null){
					Class<?> interfaceClass = invocation.getInterfaceClass();
					Object newInstance;
					Result result=null;
					try {
						newInstance = serviceObjectFindInteferce.getObject(invocation.getClassName());
						Resolver resolver = RpcProxyClient.getInvocation(newInstance);
						result = resolver.invoke(invocation);
						result.setSerialNo(invocation.getSerialNo());
//						Result result = new Result(new Person());
						result.setProtocol(invocation.getProtocol());
						result.setSerialNo(invocation.getSerialNo());
						channel.writeAndFlush(result);
					} catch (Exception e) {
						result = new Result(null,e,"没找到service", Const.ERROR_CODE);
						result.setProtocol(invocation.getProtocol());
						result.setSerialNo(invocation.getSerialNo());
						channel.writeAndFlush(new Result(result));
					}// 从spring 容器中 获取bean
					
				}
			}
		};
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		this.start();
	}

	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		if(sync!=null){
			sync.channel().close();
		}
		if(ServerdefLoopGroup!=null){
			ServerdefLoopGroup.shutdownGracefully();
		}
	}

}
