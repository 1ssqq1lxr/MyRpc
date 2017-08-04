package com.it.netty.rpc.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.core.RpcClientInit;
import com.it.netty.rpc.message.MsgBackCall;
import com.it.netty.rpc.message.MsgRequest;
import com.it.netty.rpc.message.MsgResponse;
import com.it.netty.rpc.service.Person;
/**
 * 响应handler
 * @author 17070680
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<MsgResponse>{
	
	
	private static ConcurrentHashMap<String, Object> allback = new ConcurrentHashMap<String, Object>();
	private  RpcClientInit client1;
	public RpcClientHandler(RpcClientInit client1) {
		super();
		this.client1 = client1;
	}
	private  static ChannelHandlerContext ctx;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, MsgResponse msg)
			throws Exception {
		// TODO Auto-generated method stub
		MsgBackCall back =(MsgBackCall) allback.get(msg.getSiralNo());
		back.putData(msg);
		logger.info("success accept server, port is:{}", msg.toString());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		this.ctx=ctx;
		Person p = new Person();
		p.setAge(1);
		p.setName("zhangsan");
		ctx.channel().writeAndFlush(p);
		logger.info(getClass().getName()+":ChannelHandlerContext success open:{}", ctx.toString());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		client1.connect();
		super.channelInactive(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		// TODO Auto-generated method stub
		IdleStateEvent e = (IdleStateEvent) evt; 
		if(e.state().equals(IdleState.READER_IDLE)){
			sendHeat(ctx);
			
		}
		ctx.fireUserEventTriggered(evt);
	}
	@Override
	public void exceptionCaught(
			ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		ctx.close();
	}
	private void sendHeat(ChannelHandlerContext ctx){
		
	}

	public static MsgBackCall sendMag(MsgRequest obj){
		MsgBackCall back= new MsgBackCall();
		ctx.writeAndFlush(obj);
		allback.put(obj.getSiralNo(), back);
		return back;
	}
}
