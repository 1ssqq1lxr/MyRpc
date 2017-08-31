package com.it.netty.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.filter.AbatractParameterFilter;
import com.it.netty.rpc.message.Const;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.romote.Callback;
import com.it.netty.rpc.romote.DeafultNettyClientRemoteConnection;
/**
 * rpc客户端代码类
 * @author 17070680
 *
 * @param <T>
 */
public class RpcInvocationHandler<T> implements InvocationHandler{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	

	private static ConcurrentHashMap<Object, Invocation>  map = new ConcurrentHashMap<>();
	
	private DeafultNettyClientRemoteConnection connection = DeafultNettyClientRemoteConnection.newInstance();


	private Class<T> classes;
	private AbatractParameterFilter<Invocation> filter;
	public RpcInvocationHandler(Class<T> classes,AbatractParameterFilter filter) {
		super();
		this.classes = classes;
		this.filter=filter;
	}
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Invocation invocation = filter.doParameter(method,classes,args);
		Callback invokeAsync = connection.invokeAsync(invocation);
		if(invokeAsync==null){
			logger.info(this.getClass().getName()+"连接远程服务器失败{}", invocation);
			return null;
		}
		Result result = invokeAsync.getObject();
		if(result.getResultCode().endsWith(Const.ERROR_CODE)){
			throw result.getException();
		}
		return result.getMsg();
	}


}
