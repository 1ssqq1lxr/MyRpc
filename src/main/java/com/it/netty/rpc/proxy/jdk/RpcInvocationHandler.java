package com.it.netty.rpc.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.it.netty.rpc.filter.AbatractParameterFilter;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.proxy.RpcProxyService;
/**
 * rpc客户端代码类
 * @author 17070680
 *
 * @param <T>
 */
public class RpcInvocationHandler<T> extends RpcProxyService implements InvocationHandler{
	private Class<T> classes;
	private AbatractParameterFilter<Invocation> filter;
	public RpcInvocationHandler(Class<T> classes,AbatractParameterFilter<Invocation> filter) {
		super();
		this.classes = classes;
		this.filter=filter;
	}
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
			long begintime = System.currentTimeMillis();
			String methodName = method.getName();
	        Class<?>[] parameterTypes = method.getParameterTypes();
	        if (method.getDeclaringClass() == Object.class) {
	        	return doProxy(filter, method, args);
	        }
	        if ("toString".equals(methodName) && parameterTypes.length == 0) {
	            return proxy.toString();
	        }
	        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
	            return proxy.hashCode();
	        }
	        if ("equals".equals(methodName) && parameterTypes.length == 1) {
	            return proxy.equals(args[0]);
	        }
	        Object result =doProxy(filter, method, args);
	        long endtime = System.currentTimeMillis();
//	        logger.info(msg);
	    	return result;
	}

}
