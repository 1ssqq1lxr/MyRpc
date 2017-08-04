package com.it.netty.rpc.serialize;

import java.io.Serializable;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 编码
 * @author 17070680
 *
 */
public class MyEncode extends MessageToByteEncoder {

	BaseRpcSerialize se;
	

	public MyEncode(BaseRpcSerialize se) {
		super();
		this.se = se;
	}


	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception {
		// TODO Auto-generated method stub
		MsgConventer msh = new MsgConventer();
		out.writeInt(msh.getHeader());
		byte[] encode = se.encode(msg);
		out.writeInt(encode.length);
		out.writeBytes(encode);
		
	}

}
