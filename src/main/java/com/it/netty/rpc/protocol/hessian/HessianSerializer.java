package com.it.netty.rpc.protocol.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.codec.binary.Base64;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.it.netty.rpc.protocol.Serializer;

/**
 * 
 * @author 17070680
 *
 */
@SuppressWarnings("unchecked")
public class HessianSerializer implements Serializer {

    @Override
    public byte[] serializeAsBytes(Object bean) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            Hessian2Output hessian2Output = new Hessian2Output(bout);
            hessian2Output.writeObject(bean);
            hessian2Output.close();
            return bout.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] serializeBytes) {
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(serializeBytes);
            Hessian2Input hessian2Input = new Hessian2Input(bin);
            return (T) hessian2Input.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String serializeAsString(Object bean) {
        byte[] serializeAsBytes = this.serializeAsBytes(bean);
        return Base64.encodeBase64String(serializeAsBytes);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, String serializeString) {
        byte[] serializeAsBytes = Base64.decodeBase64(serializeString);
        return this.deserialize(clazz, serializeAsBytes);
    }
}
