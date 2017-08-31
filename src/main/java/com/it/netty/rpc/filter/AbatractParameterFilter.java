package com.it.netty.rpc.filter;

import java.lang.reflect.Method;


public interface  AbatractParameterFilter<T>  {

	public abstract  T doParameter(Method method,Class<?> classt,Object[] objects);
}
