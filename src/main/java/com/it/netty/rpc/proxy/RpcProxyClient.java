package com.it.netty.rpc.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.message.Const;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Resolver;
import com.it.netty.rpc.message.Result;
/**
 * 动态代理客户端
 * @author 17070680
 *
 * @param <T>
 */
public class RpcProxyClient {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@SuppressWarnings("unchecked")
	public  static <T>  T getProxy( Class<T> classes) {
		// TODO Auto-generated method stub
		return (T) Proxy.newProxyInstance(classes.getClassLoader(), new Class<?>[]{classes}, new RpcInvocationHandler<T>(classes));
	}
	public  static <T> Resolver getInvocation(final T t) {
		// TODO Auto-generated method stub
		return new AbastractResolver<T>(t) {
			public Result doInvoke(Invocation invocation) {
				// TODO Auto-generated method stub
				try {
					Method method = t.getClass().getMethod(invocation.getMethodName(), invocation.getParamsType());
					return new Result(method.invoke(t, invocation.getParams()));
				} catch (ReflectiveOperationException e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName()+":{}",e.getMessage());
					return new Result(null, e, "操作失败", Const.ERROR_CODE);
				} 
			}


		

		};
	}


}
