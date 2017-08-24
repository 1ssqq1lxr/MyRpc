package com.it.netty.rpc.message;

public class Timeout {
	private Class<?> classt;
	private long timeout;

	public Class<?> getClasst() {
		return classt;
	}

	public void setClasst(Class<?> classt) {
		this.classt = classt;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
