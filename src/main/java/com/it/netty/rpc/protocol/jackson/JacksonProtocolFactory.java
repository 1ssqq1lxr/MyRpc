package com.it.netty.rpc.protocol.jackson;

import com.it.netty.rpc.protocol.BasicProtocolFactory;
import com.it.netty.rpc.protocol.Protocols;
import com.it.netty.rpc.protocol.Serializer;

/**
 * 
 * @author 17070680
 *
 */
public class JacksonProtocolFactory extends BasicProtocolFactory {
    private final Serializer serializer;
    
    public JacksonProtocolFactory() {
        super(Protocols.DEFAULT_PROTOCOL);
        this.serializer = new JacksonSerializer();
    }

    @Override
    public byte[] encode(Object obj) {
        return serializer.serializeAsBytes(obj);
    }

    @Override
    public <T> T decode(Class<T> objType, byte[] data) {
        return serializer.deserialize(objType, data);
    }
}
