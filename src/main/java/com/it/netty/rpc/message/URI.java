package com.it.netty.rpc.message;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class URI implements Serializable {
	/**
	 * 序列化号
	 */
	private static final long serialVersionUID = 1L;

	private transient long MINTIME = 3000L;
	private String serialMethod;  
	private String host;
	private 	int port;
	private String message;
	private Long timeout = 3000L;
	
	public URI(String serialMethod, String host, int port, String message,
			Long timeout) {
		super();
		this.serialMethod = serialMethod;
		this.host = host;
		this.port = port;
		this.message = message;
		this.timeout = timeout;
	}
	private transient  Lock lock = new ReentrantLock();
	private transient CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public boolean tryLock(long time) throws InterruptedException{
		return this.lock.tryLock(Const.TIME_OUT, TimeUnit.MILLISECONDS);
	}
	
	public void await(long time) throws InterruptedException{
		time=time==0?MINTIME:time;
		countDownLatch.await(MINTIME, TimeUnit.MILLISECONDS);
	}
	public void countDown() throws InterruptedException{
		countDownLatch.countDown();
	}
	
	
	public Long getTimeout() {
		return timeout;
	}
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
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
				+ ", port=" + port + ", message=" + message + ", timeout="
				+ timeout + "]";
	}
	
		
}
