package com.it.netty.rpc.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.io.InetAddressHandle;
import com.google.common.net.InetAddresses;
import com.it.netty.rpc.excutor.RpcExcutors;
import com.it.netty.rpc.message.MsgRequest;
import com.it.netty.rpc.message.MsgResponse;

/**
 * 请求处理handler
 * @author 17070680
 *
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<MsgRequest>{

	private static RpcExcutors exRpcExcutors = new RpcExcutors("Rpc-method-server");
	private static List<Channel> channels = new ArrayList<Channel>();
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private int num ;
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, MsgRequest request)
			throws Exception {
		System.out.println(request);
		
		exRpcExcutors.excute(new HandRequestMsg(request, ctx));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("===========================连接成功");
//		ctx.close();
		super.channelActive(ctx);
	}


	@Override
	public void exceptionCaught(
			ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		// TODO Auto-generated method stub
//		IdleStateEvent e = (IdleStateEvent) evt; 
//		if(e.state().equals(IdleState.READER_IDLE)){
//			if(num<4){
//				num++;
//			}
//			
//			
//		}
		ctx.fireUserEventTriggered(evt);
	}

}
