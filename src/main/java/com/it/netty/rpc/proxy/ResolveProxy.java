package com.it.netty.rpc.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.message.Const;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Resolver;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.proxy.jdk.RpcJdkProxyClient;

public class ResolveProxy {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	public  static  Resolver getInvocation(final Object t) {
		return new AbastractResolver(t) {
			public Result doInvoke(Invocation invocation) {
				try {
					Method method = t.getClass().getMethod(invocation.getMethodName(), invocation.getParamsType());
					return new Result(method.invoke(t, invocation.getParams()));
				} catch (ReflectiveOperationException e) {
					logger.error(this.getClass().getName()+":{}",e.getMessage());
					return new Result(null, e, "操作失败", Const.ERROR_CODE);
				} 
			}
		};
	}
}
