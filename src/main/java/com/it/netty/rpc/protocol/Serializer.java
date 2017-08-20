package com.it.netty.rpc.protocol;

/**
 * 
 * @author 17070680
 *
 */
public interface Serializer {
	String serializeAsString(Object bean);

	byte[] serializeAsBytes(Object bean);

	<T> T deserialize(Class<T> clazz, String serializeString);

	<T> T deserialize(Class<T> clazz, byte[] serializeBytes);
}
