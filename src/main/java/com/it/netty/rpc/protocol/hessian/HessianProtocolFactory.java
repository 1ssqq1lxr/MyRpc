package com.it.netty.rpc.protocol.hessian;

import com.it.netty.rpc.protocol.BasicProtocolFactory;
import com.it.netty.rpc.protocol.Protocols;
import com.it.netty.rpc.protocol.Serializer;

/**
 * 
 * @author 17070680
 *
 */
public class HessianProtocolFactory extends BasicProtocolFactory {
    private final Serializer serializer;

    public HessianProtocolFactory() {
        super(Protocols.HESSIAN_PROTOCOL);
        this.serializer = new HessianSerializer();
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
