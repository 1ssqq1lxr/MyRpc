package com.it.netty.rpc.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author 17070680
 *
 */
public abstract class BasicProtocolFactory implements ProtocolFactory {
    protected final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final int protocol;

    protected BasicProtocolFactory(int protocol) {
        super();
        this.protocol = protocol;
    }

    @Override
    public final int getProtocol() {
        return this.protocol;
    }
}