package com.it.netty.rpc.serialize;
/**
 * 后期加入可选序列化
 * @author 17070680
 *
 */
public interface BaseRpcSerialize {
	byte[]  encode();
	Object decode();
}
