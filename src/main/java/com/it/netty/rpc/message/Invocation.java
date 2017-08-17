package com.it.netty.rpc.message;

public interface Invocation {
		
	public URI getURI();
	
	public Result invoke(Invocation invocation);
}
