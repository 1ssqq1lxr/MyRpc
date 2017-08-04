package com.it.netty.rpc.message;

import java.io.Serializable;
/**
 * 请求实体类
 * @author 17070680
 *
 */
public class MsgRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1000000L;
	private String siralNo;
	private Class<?> classt;
	private String methodName;
	private Object[] params;
	public String getSiralNo() {
		return siralNo;
	}
	public void setSiralNo(String siralNo) {
		this.siralNo = siralNo;
	}
	public Class getClasst() {
		return classt;
	}
	public void setClasst(Class classt) {
		this.classt = classt;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	
}
