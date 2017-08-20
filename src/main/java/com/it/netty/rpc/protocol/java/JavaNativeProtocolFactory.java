package com.it.netty.rpc.protocol.java;

import com.it.netty.rpc.protocol.BasicProtocolFactory;
import com.it.netty.rpc.protocol.Protocols;
import com.it.netty.rpc.protocol.Serializer;

/**
 * 
 * @author 17070680
 *
 */
public class JavaNativeProtocolFactory extends BasicProtocolFactory {
    private final Serializer serializer;

    public JavaNativeProtocolFactory() {
        super(Protocols.JAVA_NATIVE_PROTOCOL);
        this.serializer = new JavaNativeSerializer();
    }

    @Override
    public byte[] encode(Object obj) {
        return this.serializer.serializeAsBytes(obj);
    }

    @Override
    public <T> T decode(Class<T> objType, byte[] data) {
        return this.serializer.deserialize(objType, data);
    }
}
