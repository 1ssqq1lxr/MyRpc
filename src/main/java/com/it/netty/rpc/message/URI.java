package com.it.netty.rpc.message;

import java.io.Serializable;


public class URI implements Serializable {
	/**
	 * 序列化号
	 */
	private static final long serialVersionUID = 1L;
	String serialMethod;  
	String host;
	int port;
	String message;
	public String getSerialMethod() {
		return serialMethod;
	}
	public void setSerialMethod(String serialMethod) {
		this.serialMethod = serialMethod;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
