package com.it.netty.rpc.romote;

import io.netty.bootstrap.Bootstrap;
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
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;
import java.util.Set;
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
	static class staticInitBean{
		public static DeafultNettyClientRemoteConnection clientRemoteConnection = new DeafultNettyClientRemoteConnection();
	}
	public static DeafultNettyClientRemoteConnection newInstance(){
		return staticInitBean.clientRemoteConnection;
	}
	private  DeafultNettyClientRemoteConnection() {
		//		super(port);
		this.resource();
		this.start();
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
	public Bootstrap start() {
		b.group(clientGroup)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_KEEPALIVE, false)
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
		.option(ChannelOption.SO_SNDBUF, 65535)
		.option(ChannelOption.SO_RCVBUF, 65535)
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
			
		// TODO Auto-generated method stub
			try {
				if(this.lock.tryLock(Const.TIME_OUT, TimeUnit.MILLISECONDS)){
					
					ChannelManager channelManager = DeafultNettyClientRemoteConnection.channels.get(getRemoteStr(uri));
					if(channelManager==null){
						final ChannelFuture connect = b.connect(uri.getHost(), uri.getPort()).sync();
						if(connect.isSuccess()){
							ChannelManager channelManager1 = new ChannelManager(connect,uri);
							DeafultNettyClientRemoteConnection.channels.putIfAbsent(getRemoteStr(uri), channelManager1);
							return channelManager1;
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
	class OutChannelInvocationHandler extends SimpleChannelInboundHandler<Result>{

		@Override
		protected void messageReceived(ChannelHandlerContext ctx, Result result)
				throws Exception {
			// TODO Auto-generated method stub
			setCallBack(result);
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
			if(invocation instanceof Invocation){ // 请求
				ProtocolFactory protocolFactory = getProtocolFactory((Invocation)invocation);
				System.out.println(TOP_LENGTH+"");
				out.writeInt(TOP_LENGTH);
				byte[] encode = protocolFactory.encode(invocation);
				out.writeInt(protocolFactory.getProtocol());
				out.writeInt(encode.length);
				out.writeBytes(encode);
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
		
	
	
			
	}
	class ClinetDecode extends ByteToMessageDecoder{

		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf in,
				List<Object> out) throws Exception {
			int readIndex = in.readerIndex();
			if(in.readableBytes()<4){
				return;
			}
			int readInt = in.readInt();
			if(readInt!=TOP_LENGTH){
				log.warn(this.getClass().getName()+"消息协议头非法{}", readInt);
				return;
			}
			int protocol = in.readInt();
			int bodylength = in.readInt();
			if(bodylength>in.readableBytes()){
				in.readerIndex(readIndex); //初始化读取位置
				return;
			}
			byte[] bytes = new byte[bodylength];
			ProtocolFactory select = protocolFactorySelector.select(protocol);
			in.readBytes(bytes);
			Result decode = select.decode(Result.class, bytes);
			out.add(decode);
		}
	}
	
	
}
