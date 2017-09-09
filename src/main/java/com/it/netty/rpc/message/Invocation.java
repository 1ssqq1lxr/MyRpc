package com.it.netty.rpc.message;

import java.io.Serializable;
import java.util.Arrays;

public class Invocation implements Serializable{
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 12223L;
	private String serialNo;
	private long timeout =3000;
	private Class<?> interfaceClass;
	private String className;
	private String methodName;
	private Object[] params;
	private Class<?>[] paramsType;
	private String protocol ="DEFAULTSERIALIZE";
	private boolean returnType =true;
	private transient URI uri;
	public boolean isReturnType() {
		return returnType;
	}

	public void setReturnType(boolean returnType) {
		this.returnType = returnType;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
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
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}
	@Override
	public String toString() {
		return "Invocation [serialNo=" + serialNo + ", timeout=" + timeout
				+ ", interfaceClass=" + interfaceClass + ", className="
				+ className + ", methodName=" + methodName + ", params="
				+ Arrays.toString(params) + ", paramsType="
				+ Arrays.toString(paramsType) + ", protocol=" + protocol
				+ ", uri=" + uri + "]";
	}
	
	
	
}
