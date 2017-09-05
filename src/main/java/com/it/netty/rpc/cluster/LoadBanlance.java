package com.it.netty.rpc.cluster;

import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.zookeeper.RemoteAddress;

public interface LoadBanlance {
	public URI selectRandom(RemoteAddress[]  uris);
	public String getName();
}
