package com.it.netty.rpc.protocol;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.it.netty.rpc.protocol.hessian.HessianProtocolFactory;
import com.it.netty.rpc.protocol.jackson.JacksonProtocolFactory;
import com.it.netty.rpc.protocol.java.JavaNativeProtocolFactory;

/**
 * 
 * @author 17070680
 *
 */
public class DefaultProtocolFactorySelector implements ProtocolFactorySelector {
    private static final Logger log = LoggerFactory.getLogger(DefaultProtocolFactorySelector.class.getSimpleName());
    private ConcurrentHashMap<Integer, ProtocolFactory> protocolFactories = new ConcurrentHashMap<Integer, ProtocolFactory>();

    public DefaultProtocolFactorySelector() {
        super();
        this.registry(new JacksonProtocolFactory());
        this.registry(new JavaNativeProtocolFactory());
        this.registry(new HessianProtocolFactory());
    }

    @Override
    public ProtocolFactory select(int protocol) {
        ProtocolFactory protocolFactory = protocolFactories.get(protocol);
        return protocolFactory;
    }

    public ProtocolFactory registry(ProtocolFactory protocolFactory) {
        ProtocolFactory pf = protocolFactories.putIfAbsent(protocolFactory.getProtocol(), protocolFactory);
        return pf;
    }

    public void setProtocolFactories(List<ProtocolFactory> protocolFactories) {
        if (CollectionUtils.isEmpty(protocolFactories)) {
            return;
        }
        for (ProtocolFactory protocolFactory : protocolFactories) {
            ProtocolFactory pf = this.protocolFactories.putIfAbsent(protocolFactory.getProtocol(), protocolFactory);
        }
    }
}
