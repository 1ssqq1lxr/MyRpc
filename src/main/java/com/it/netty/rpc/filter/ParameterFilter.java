package com.it.netty.rpc.filter;

import java.lang.reflect.Method;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.cluster.LoadBanlanceSelecter;
import com.it.netty.rpc.exception.NoFindClassException;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.proxy.Proxy;
import com.it.netty.rpc.proxy.RpcProxyFactorySelecter;
import com.it.netty.rpc.zookeeper.ZookeeperOpenApi;

public class ParameterFilter implements AbatractParameterFilter<Invocation>{
	protected final  Logger logger = LoggerFactory.getLogger(getClass());
	private RpcProxyFactorySelecter factorySelecter = new RpcProxyFactorySelecter();
	private LoadBanlanceSelecter banlanceSelecter = new LoadBanlanceSelecter();
	private String protocol;
	private String proxy ;
	private String loadBanlance;
	private ZookeeperOpenApi api = new ZookeeperOpenApi();
	private int clientGroup_thread_nums ;
	public ParameterFilter(){
		super();
	}
	public Proxy getDefaultProxy(){
		return factorySelecter.selectProxy(this.proxy);
	}
	public Invocation doParameter(Method method,Object[] params) {
		Class<?> declaringClass = method.getDeclaringClass();
		Invocation invocation = new Invocation();
		invocation.setParams(params);
		invocation.setProtocol(protocol);
		invocation.setClassName(declaringClass.getName());
		invocation.setInterfaceClass(declaringClass);
		invocation.setSerialNo(UUID.randomUUID().toString());
		invocation.setParamsType(method.getParameterTypes());
		invocation.setMethodName(method.getName());
		invocation.setTimeout(5000);
		Class<?> returnType = method.getReturnType();
		if(returnType.getName().equals("void"))
			invocation.setReturnType(false);
		URI uri = api.getURI(banlanceSelecter.selectLoadBanlance(loadBanlance),declaringClass.getName());
		if(uri==null)
			throw new NoFindClassException(declaringClass.getName()+":未找到匹配的URI");
		invocation.setUri(uri);
		logger.info("success :{}",invocation);
		return invocation;
	}
	public String getLoadBanlance() {
		return loadBanlance;
	}
	public void setLoadBanlance(String loadBanlance) {
		this.loadBanlance = loadBanlance;
	}
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
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
}
