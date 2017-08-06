package com.it.netty.rpc.handler;

import io.netty.channel.ChannelHandlerContext;

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
		// TODO Auto-generated method stub
		MsgResponse<Person> response = new MsgResponse<Person>();
		Person p = new Person();
		p.setAge(1);
		p.setName("zhangsan");
		response.setData(p);
		response.setSiralNo(request.getSiralNo());
		ctx.writeAndFlush(response);
	}

}
