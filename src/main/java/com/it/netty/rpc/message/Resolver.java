package com.it.netty.rpc.message;

public interface Resolver {
		
	
	public Result invoke(Invocation invocation);
	
}
