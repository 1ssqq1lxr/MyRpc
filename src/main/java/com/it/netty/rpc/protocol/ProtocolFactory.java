package com.it.netty.rpc.protocol;


/**
 * 
 * @author 17070680
 *
 */
public interface ProtocolFactory {
    int getProtocol();
    byte[] encode(Object obj);
    <T> T decode(Class<T> objType, byte[] data);
}
