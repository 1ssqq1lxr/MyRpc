package com.it.netty.rpc.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.Serializable;
import java.util.List;

import com.it.netty.rpc.serialize.java.ByteObjConverter;
/**
 * 解码
 * @author 17070680
 *
 */
public class MyDecode extends ByteToMessageDecoder{
	BaseRpcSerialize se = new ByteObjConverter();
	


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		MsgConventer msh = new MsgConventer();
		int readerIndex = in.readerIndex();
		int readInt = in.readInt();
		Long length = in.readLong();
		if(in.readableBytes()<length){
		    in.readerIndex(readerIndex);  //刷新指针
            return;  
		}
		Object byteToObject =  se.decode(ByteObjConverter.read(in));
		out.add(byteToObject);
	}
	
	
}
