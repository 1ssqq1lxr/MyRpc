package com.it.netty.rpc.filter;

import java.lang.reflect.Method;
import java.util.UUID;

import com.it.netty.rpc.Config;
import com.it.netty.rpc.exception.NoFindClassException;
import com.it.netty.rpc.framework.HandlerService;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.URI;

public class ParameterFilter implements AbatractParameterFilter<Invocation>{
	private String protocol;
	
	private int clientGroup_thread_nums ;
	
	public int getClientGroup_thread_nums() {
		return clientGroup_thread_nums;
	}
	public void setClientGroup_thread_nums(int clientGroup_thread_nums) {
		this.clientGroup_thread_nums = clientGroup_thread_nums;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	ParameterFilter(){
		super();
	}
	public Invocation doParameter(Method method,Class<?> class1,Object[] params) {
		Invocation invocation = new Invocation();
		invocation.setParams(params);
		invocation.setProtocol(protocol);
		invocation.setClassName(class1.getName());
		invocation.setInterfaceClass(class1);
		invocation.setSerialNo(UUID.randomUUID().toString());
		invocation.setParamsType(method.getParameterTypes());
		invocation.setMethodName(method.getName());
		invocation.setTimeout(5000);
		URI uri = Config.uri.getCache(class1.getName());
		if(uri==null)
			throw new NoFindClassException(class1.getName());
		invocation.setUri(uri);
		return invocation;
	}
}
