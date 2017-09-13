package com.it.netty.rpc.util;

import com.it.netty.rpc.message.Invocation;

public class InvocationUtils {
		public static String formateInvokerInfo(Invocation invocation){
			StringBuilder sb = new StringBuilder();
			sb.append(invocation.getClassName()).append(".").append(invocation.getMethodName()).append("(");
			Object[] params = invocation.getParams();
			for(Object obj:params){
				sb.append("【").append(obj.getClass().getName()).append(":").append(obj).append("】");
			}
			sb.append(")");
			return sb.toString();
		} 
//		public static void main(String[] args) {
//			Invocation invocation = new Invocation();
//			invocation.setClassName("test.service");
//			invocation.setMethodName("getName");
//			invocation.setParams(new Object[]{1,"123"});
//			System.out.println(InvocationUtils.formateInvokerInfo(invocation));
//		}
}
