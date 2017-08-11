package com.it.netty.rpc.handler;

import io.netty.channel.ChannelHandlerContext;

import com.it.netty.rpc.common.ReflectUtils;
import com.it.netty.rpc.message.MsgBackCall;
import com.it.netty.rpc.message.MsgRequest;
import com.it.netty.rpc.message.MsgResponse;
import com.it.netty.rpc.service.Person;

public class HandRequestMsg implements Runnable{
	private MsgRequest request;
	private ChannelHandlerContext ctx;
	

	public HandRequestMsg(MsgRequest request, ChannelHandlerContext ctx) {
		super();
		this.request = request;
		this.ctx = ctx;
	}


	public void run() {
		// TODO Auto-generated method stu
		if(request.getReturnType().getSimpleName().equals("void")){
			ReflectUtils.getReturn(request);
		}
		else{
			MsgResponse<Object> response = new MsgResponse<Object>();
			response.setSiralNo(request.getSiralNo());
			Object return1 = ReflectUtils.getReturn(request);
			response.setData(return1);
			ctx.writeAndFlush(response);
		}

	}

}
