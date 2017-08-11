package com.it.netty.rpc.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.it.netty.rpc.message.MsgRequest;

public class ReflectUtils {
	public static Object getReturn(MsgRequest request){
		String className = request.getClassName();
		String methodName = request.getMethodName();
		Object[] params = request.getParams();
		try {
			Class<?> forName = Class.forName(className);
			Object newInstance = forName.newInstance();
			Method method = forName.getMethod(methodName, request.getParamsType());
			return  method.invoke(newInstance, params);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
