package com.it.netty.rpc.proxy;

import com.it.netty.rpc.cache.Cache;
import com.it.netty.rpc.cache.CacheFactory;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.message.URI;

public abstract class AbstractInvocation implements Invocation {
	
	private URI  uri;
	private Class<?> interfaceClass;
	
	private String siralNo;
	private String className;
	private String methodName;
	private Object[] params;
	private Class<?>[] paramsType;
	private Class<?> returnType;
	private String protocol;
	private static Cache cache = new CacheFactory();
	@Override
	public URI getURI() {
		// TODO Auto-generated method stub
		return this.uri;
	}

	@Override
	public Result invoke(Invocation invocation) {
		// TODO Auto-generated method stub
		return null;
	}
	public abstract Result doInvoke();	
	


	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}
	

	public String getSiralNo() {
		return siralNo;
	}

	public void setSiralNo(String siralNo) {
		this.siralNo = siralNo;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public Class<?>[] getParamsType() {
		return paramsType;
	}

	public void setParamsType(Class<?>[] paramsType) {
		this.paramsType = paramsType;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}


}
