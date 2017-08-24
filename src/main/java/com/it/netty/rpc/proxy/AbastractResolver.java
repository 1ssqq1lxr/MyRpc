package com.it.netty.rpc.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Resolver;
import com.it.netty.rpc.message.Result;

public abstract class AbastractResolver implements Resolver {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	private transient Object t; // 实体类

	
	private  Invocation invocation;
		

	public AbastractResolver(Object t) {
		super();
		this.t = t;
	}

	public Object getT() {
		return t;
	}

	public void setT(Object t) {
		this.t = t;
	}

	@Override
	public Result invoke(Invocation invocation) {
		if(invocation.getClass().isAssignableFrom(t.getClass())){
			this.invocation=invocation;
			// TODO Auto-generated method stub
			return doInvoke(invocation);
		}
		return null;

	}
	public abstract Result doInvoke(Invocation invocation);	
	


	


}
