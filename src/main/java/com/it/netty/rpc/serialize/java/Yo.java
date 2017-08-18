package com.it.netty.rpc.serialize.java;

import java.io.Serializable;

import com.it.netty.rpc.serialize.BaseRpcSerialize;

public class Yo implements Serializable{
	public Class<?> s;
	private transient BaseRpcSerialize baseRpcSerialize;
	
	
	public BaseRpcSerialize getBaseRpcSerialize() {
		return baseRpcSerialize;
	}

	public void setBaseRpcSerialize(BaseRpcSerialize baseRpcSerialize) {
		this.baseRpcSerialize = baseRpcSerialize;
	}

	public Yo(Class<?> s) {
		super();
		this.s = s;
	}

	public Class<?> getS() {
		return s;
	}

	public void setS(Class<?> s) {
		this.s = s;
	}
	
}
