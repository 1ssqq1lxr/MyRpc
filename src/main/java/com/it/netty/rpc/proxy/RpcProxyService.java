package com.it.netty.rpc.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.filter.AbatractParameterFilter;
import com.it.netty.rpc.filter.ParameterFilter;
import com.it.netty.rpc.message.Const;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.romote.Callback;
import com.it.netty.rpc.romote.DeafultNettyClientRemoteConnection;

public class RpcProxyService {
		protected final  Logger logger = LoggerFactory.getLogger(getClass());
		public  Object doProxy(AbatractParameterFilter<Invocation> filter,Method method,Object[] args) throws Exception{
			ParameterFilter p = (ParameterFilter)filter;
			Invocation invocation = filter.doParameter(method,args);
			Callback callback = DeafultNettyClientRemoteConnection.newInstance(p.getClientGroup_thread_nums()).invokeSync(invocation); // 同步调用
			if(callback==null){
				return null;
			}
			Result result = callback.getObject();
			if(result.getResultCode().endsWith(Const.ERROR_CODE)){
				throw result.getException();
			}
			return  result.getMsg();
		}
}
