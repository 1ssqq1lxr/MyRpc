package com.it.netty.rpc.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Resolver;
import com.it.netty.rpc.message.Result;

public abstract class AbastractResolver<T> implements Resolver {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	private transient T t; // 实体类

	
	private  Invocation invocation;
		

	public AbastractResolver(T t) {
		super();
		this.t = t;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	@Override
	public Result invoke(Invocation invocation) {
		this.invocation=invocation;
		// TODO Auto-generated method stub
		return doInvoke(invocation);
	}
	public abstract Result doInvoke(Invocation invocation);	
	


	


}
