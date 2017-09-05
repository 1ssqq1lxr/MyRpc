package com.it.netty.rpc.filter;

import java.lang.reflect.Method;
import java.util.UUID;

import com.it.netty.rpc.exception.NoFindClassException;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.proxy.Proxy;
import com.it.netty.rpc.proxy.RpcProxyFactorySelecter;
import com.it.netty.rpc.zookeeper.ZookeeperOpenApi;

public class ParameterFilter implements AbatractParameterFilter<Invocation>{
	private RpcProxyFactorySelecter factorySelecter = new RpcProxyFactorySelecter();
	private String protocol;
	private String proxy ;
	private ZookeeperOpenApi api = new ZookeeperOpenApi();
	public Proxy getDefaultProxy(){
		return factorySelecter.selectProxy(this.proxy);
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
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
	public ParameterFilter(){
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
		URI uri = api.getURI(class1.getName());
		if(uri==null)
			throw new NoFindClassException(class1.getName()+":未找到匹配的URI");
		invocation.setUri(uri);
		return invocation;
	}
}
