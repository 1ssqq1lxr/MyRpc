package com.it.netty.rpc.message;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class URI implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 序列化号
	 */
	private transient long MINTIME = 3000l;
	String serialMethod;  
	String host;
	int port;
	String message;
	private transient CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public void await(long time) throws InterruptedException{
		time=time==0?MINTIME:time;
		countDownLatch.await(MINTIME, TimeUnit.MILLISECONDS);
	}
	public void countDown() throws InterruptedException{
		countDownLatch.countDown();
	}
	
	
	public URI() {
		super();
	}
	public URI(String serialMethod, String host, int port, String message) {
		super();
		this.serialMethod = serialMethod;
		this.host = host;
		this.port = port;
		this.message = message;
	}
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
	@Override
	public String toString() {
		return "URI [serialMethod=" + serialMethod + ", host=" + host
				+ ", port=" + port + ", message=" + message + "]";
	}
		
}
