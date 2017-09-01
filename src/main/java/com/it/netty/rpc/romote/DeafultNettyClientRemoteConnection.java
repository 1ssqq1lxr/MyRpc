package com.it.netty.rpc.romote;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.it.netty.rpc.heart.HeartBeat;
import com.it.netty.rpc.message.Const;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.protocol.DefaultProtocolFactorySelector;
import com.it.netty.rpc.protocol.ProtocolFactory;
import com.it.netty.rpc.protocol.ProtocolFactorySelector;
import com.it.netty.rpc.protocol.SerializeEnum;
/**
 * 
 * @author 17070680
 *
 */
public class DeafultNettyClientRemoteConnection  extends NettyClientApiService{
	private  final  ProtocolFactorySelector protocolFactorySelector = new DefaultProtocolFactorySelector();
	private  DefaultEventLoopGroup ClientdefLoopGroup;
	private NioEventLoopGroup clientGroup;
	private Bootstrap b;
	private final Lock lock = new ReentrantLock();
	private final int TOP_LENGTH=129>>1|34; // 数据协议头
	private final int TOP_HEARTBEAT=129>>1|36; // 心跳协议头
	private  final  CountDownLatch countDownLatch = new CountDownLatch(1);
	private static int threads;
	static class staticInitBean{
		public static DeafultNettyClientRemoteConnection clientRemoteConnection = new DeafultNettyClientRemoteConnection();
	}
	public static DeafultNettyClientRemoteConnection newInstance(int threads){
		DeafultNettyClientRemoteConnection.threads=threads;
		return staticInitBean.clientRemoteConnection;
	}
	private  DeafultNettyClientRemoteConnection() {
		this.resource();
		this.start();
	}

	public void resource(){
		this.ClientdefLoopGroup = new DefaultEventLoopGroup(threads, new ThreadFactory() {
			private AtomicInteger index = new AtomicInteger(0);

			public Thread newThread(Runnable r) {
				return new Thread(r, "sxsDEFAULTEVENTLOOPGROUP_" + index.incrementAndGet());
			}
		});
		this.clientGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()*2, new ThreadFactory() {
			private AtomicInteger index = new AtomicInteger(0);

			public Thread newThread(Runnable r) {
				return new Thread(r, "Client_" + index.incrementAndGet());
			}
		});

		this.b = new Bootstrap();
	}
	public Bootstrap start() {
		b.group(clientGroup)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_KEEPALIVE, false)
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
		.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
		.option(ChannelOption.SO_SNDBUF, 10*1024*1024)
		.option(ChannelOption.SO_RCVBUF, 10*1024*1024)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(ClientdefLoopGroup);
				ch.pipeline().addLast(new ClientEncode());
				ch.pipeline().addLast(new ClinetDecode());
				ch.pipeline().addLast(new IdleStateHandler(20, 0, 0));
				ch.pipeline().addLast(new OutChannelInvocationHandler());

			}
		});
		return b;
	}
	@Override
	public ChannelManager doConnect(final URI uri) {
		try {
			ChannelManager  	channelManager =null;
			if(this.lock.tryLock(Const.TIME_OUT, TimeUnit.MILLISECONDS)){
				
				channelManager=DeafultNettyClientRemoteConnection.channels.get(getRemoteStr(uri));
				if(channelManager==null){
					final ChannelFuture connect = b.connect(uri.getHost(), uri.getPort()).sync();
					if(connect.isSuccess()){
						ChannelManager channelManager1 = new ChannelManager(connect,uri);
						DeafultNettyClientRemoteConnection.channels.putIfAbsent(getRemoteStr(uri), channelManager1);
						uri.countDown();
						return channelManager1;

					}
					log.error(this.getClass().getName()+" 连接｛｝ 失败",getRemoteStr(uri));
				}
				else
					return channelManager;

			}
			uri.await(0);
			channelManager=DeafultNettyClientRemoteConnection.channels.get(getRemoteStr(uri));
			if(channelManager!=null)
				return channelManager;
		} catch (InterruptedException e) {
			log.error(this.getClass().getName(), e);
		}

		return null;


	}
	class OutChannelInvocationHandler extends SimpleChannelInboundHandler<Result>{

		@Override
		protected void messageReceived(ChannelHandlerContext ctx, Result result)
				throws Exception {
			setCallBack(result);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
			log.info(this.getClass().getName()+"channel 关闭{}：{}", ctx.channel(),cause);
			ctx.close();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			log.info(this.getClass().getName()+"channel 关闭{}", ctx.channel());
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
			IdleStateEvent e = (IdleStateEvent) evt; 
			if(e.state().equals(IdleState.READER_IDLE) 
					||e.state().equals(IdleState.WRITER_IDLE) 
					||e.state().equals(IdleState.ALL_IDLE) 
					){
				ctx.writeAndFlush(new HeartBeat());

			}
			super.userEventTriggered(ctx, evt);
		}



	}
	class ClientEncode extends MessageToByteEncoder<Object> {
		protected void encode(ChannelHandlerContext ctx, Object invocation, ByteBuf out)
				throws Exception {
			if(invocation instanceof Invocation){ 
				ByteBuffer byteBuffer = this.getByteBuffer((Invocation)invocation);
				out.writeBytes(byteBuffer);
			}
			else if(invocation instanceof HeartBeat){// 心跳
				ProtocolFactory protocolFactory =protocolFactorySelector.select(SerializeEnum.DEFAULTSERIALIZE.getValue());
				out.writeInt(TOP_HEARTBEAT);
				out.writeInt(SerializeEnum.DEFAULTSERIALIZE.getValue());
				byte[] encode = protocolFactory.encode((HeartBeat)invocation);
				out.writeInt(encode.length);
				out.writeBytes(encode);
			}
		}
		protected ProtocolFactory getProtocolFactory(Invocation invocation){
			SerializeEnum valueOf = SerializeEnum.valueOf(invocation.getProtocol());
			valueOf=valueOf==null?SerializeEnum.DEFAULTSERIALIZE:valueOf;
			int serialNo = valueOf.getValue();
			ProtocolFactory protocolFactory = protocolFactorySelector.select(serialNo);
			return protocolFactory;
		}
		private ByteBuffer getByteBuffer(Invocation invocation){
			ProtocolFactory protocolFactory = getProtocolFactory((Invocation)invocation);
			byte[] encode = protocolFactory.encode(invocation);
			ByteBuffer headerBytes = ByteBuffer.allocate(4+4+4+encode.length);
			headerBytes.putInt(TOP_LENGTH);
			headerBytes.putInt(protocolFactory.getProtocol());
			headerBytes.putInt(encode.length);
			headerBytes.put(encode);
			headerBytes.flip();
			return headerBytes;
		}
	}
	class  ClinetDecode extends ByteToMessageDecoder{
		@Override
		protected void  decode(ChannelHandlerContext ctx, ByteBuf in,
				List<Object> out) throws Exception {
		    int beginReader;  
		    if (in.readableBytes() < 12) {  
                return;  
            }
	        while (true) {  
	                beginReader = in.readerIndex();  
	                in.markReaderIndex();  
	                int  s =in.readInt();
	            	log.info("header :{}:{},{}",in,in.hashCode(),s);
	                if ( s== TOP_LENGTH) {  
	                    break;  
	                }
	                in.resetReaderIndex();  
	                in.readByte();    
	         } 
			int protocol = in.readInt();
			int bodylength = in.readInt();
			if(bodylength>in.readableBytes()){
				in.readerIndex(beginReader); //初始化读取位置
				return;
			}
			byte[] bytes = new byte[bodylength];
			ProtocolFactory select = protocolFactorySelector.select(protocol);
			in.readBytes(bytes);
			Result decode = select.decode(Result.class, bytes);
			log.info("success  do  request {}:{}",ClinetDecode.this,decode);
			out.add(decode);
		}
	}


}
