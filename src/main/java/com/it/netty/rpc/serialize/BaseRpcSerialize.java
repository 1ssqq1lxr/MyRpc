package com.it.netty.rpc.serialize;
/**
 * 后期加入可选序列化
 * @author 17070680
 *
 */
public interface BaseRpcSerialize {
	byte[]  encode(Object obj);
	Object decode(byte[] bytes);
	abstract class taskInit{
		abstract BaseRpcSerialize getRpcSerialize();
	}
}
