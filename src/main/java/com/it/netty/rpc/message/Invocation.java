package com.it.netty.rpc.message;

public interface Invocation {
		
	public URI getURI();
	
	public Result invoke(Invocation invocation);
	
	public String getMethodName();
//	
	public Class<?>[] getParamsType();
	
	public Object[] getParams();
	
	public Invocation getInvocation(String url);
}
