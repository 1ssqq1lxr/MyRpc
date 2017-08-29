package com.it.netty.rpc.romote;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
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

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import com.it.netty.rpc.service.ServiceObjectFindInteferce;
/**
 * 
 * @author 17070680
 *
 */
public class DeafultNettyServerRemoteConnection extends NettyServerApiService implements InitializingBean {

	ServiceObjectFindInteferce serviceObjectFindInteferce ;



//	private final Lock lock = new ReentrantLock();
//	private final Condition condition = lock.newCondition();
	public ServiceObjectFindInteferce getServiceObjectFindInteferce() {
		return serviceObjectFindInteferce;
	}

	public void setServiceObjectFindInteferce(
			ServiceObjectFindInteferce serviceObjectFindInteferce) {
		this.serviceObjectFindInteferce = serviceObjectFindInteferce;
	}
	private RpcProxyClient init = new RpcProxyClient();
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
		ServerdefLoopGroup = new DefaultEventLoopGroup(Runtime.getRuntime().availableProcessors() * 200, new ThreadFactory() {
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
		workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 200, new ThreadFactory() {
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
		.option(ChannelOption.SO_REUSEADDR, true)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_BACKLOG, 1024)
		.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
		.option(ChannelOption.SO_SNDBUF, 10*1024*1024)
		.option(ChannelOption.SO_RCVBUF, 10*1024*1024)
		.childHandler(new ChannelInitializer<SocketChannel>() {
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(ServerdefLoopGroup);
				ch.pipeline().addLast(new ServerEncode());
				ch.pipeline().addLast(new ServerDecode());
				ch.pipeline().addLast(new IdleStateHandler(30,30 ,30));
				ch.pipeline().addLast(new OutChannelInvocationHandler());
			}
		}) 
		.childOption(ChannelOption.SO_KEEPALIVE, true)
		.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
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
			log.info(this.getClass().getName()+"channel 关闭{}：{}", ctx.channel(),cause);
			ctx.close();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			log.info(this.getClass().getName()+"channel 关闭{}", ctx.channel());
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
					log.info(this.getClass().getName()+"{} be remved",channel);
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
//	private final Lock locks = new ReentrantLock();
	class ServerEncode extends MessageToByteEncoder<Object> {

		protected void encode(ChannelHandlerContext ctx, Object invocation, ByteBuf out)
				throws Exception {
			if(invocation instanceof Result){ // 请求
//				ByteBuf directBuffer = Unpooled.buffer();
				ByteBuffer byteBuffer = this.getByteBuffer((Result)invocation);
//				directBuffer.writeBytes(byteBuffer);
				out.writeBytes(byteBuffer);
				log.info("success  do  request {}:{}",ctx.channel(),out.hashCode());
			}

		}
		protected ProtocolFactory getProtocolFactory(Result result){
			SerializeEnum valueOf = SerializeEnum.valueOf(result.getProtocol());
			valueOf=valueOf==null?SerializeEnum.DEFAULTSERIALIZE:valueOf;
			int serialNo = valueOf.getValue();
			ProtocolFactory protocolFactory = protocolFactorySelector.select(serialNo);
			return protocolFactory;
		}
		private ByteBuffer getByteBuffer(Result result){
			ProtocolFactory protocolFactory = getProtocolFactory(result);
			byte[] encode = protocolFactory.encode(result);
			ByteBuffer headerBytes = ByteBuffer.allocate(4+4+4+encode.length);
			headerBytes.putInt(TOP_LENGTH);
			headerBytes.putInt(protocolFactory.getProtocol());
			headerBytes.putInt(encode.length);
			headerBytes.put(encode);
			headerBytes.flip();
			return headerBytes;
		}

	}

	class ServerDecode extends ByteToMessageDecoder{
		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf in,
				List<Object> out) throws Exception {
			if(in.readableBytes()<4){
				return;
			}
			int beginReader; 
			int header; 
			while (true) {  
				beginReader = in.readerIndex();  
				in.markReaderIndex();  
				header=in.readInt();
				log.info("header :{}:{},{}",in,in.hashCode(),header);
				if (header == TOP_LENGTH || header==TOP_HEARTBEAT) {  
					break;  
				}
				in.resetReaderIndex();  
				in.readByte();  
				if (in.readableBytes() < 4) {  
					return;  
				}  
			} 
			if(header==TOP_LENGTH){ // 处理消息
				int protocol = in.readInt();
				int bodylength = in.readInt();
				if(bodylength>in.readableBytes()){
					in.readerIndex(beginReader); //初始化读取位置
					return;
				}
				byte[] bytes = new byte[bodylength];
				ProtocolFactory select = protocolFactorySelector.select(protocol);
				in.readBytes(bytes);
				in.discardReadBytes();
				Invocation invocation = select.decode(Invocation.class, bytes);
				out.add(invocation);
			}
			else if(header==TOP_HEARTBEAT){ // 心跳
				int protocol = in.readInt();
				int bodylength = in.readInt();
				if(bodylength>in.readableBytes()){
					in.readerIndex(beginReader); //初始化读取位置
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
	private final Lock lock1 = new ReentrantLock();
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
						Resolver resolver = init.getInvocation(newInstance);
						result = resolver.invoke(invocation);
						result.setSerialNo(invocation.getSerialNo());
						result.setProtocol(invocation.getProtocol());
						result.setSerialNo(invocation.getSerialNo());
						channel.writeAndFlush(result);
						//						log.info("success  do  request {}:{}",channel,invocation);
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
