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
	private String className;
	private String methodName;
	private Object[] params;
	public String getSiralNo() {
		return siralNo;
	}
	public void setSiralNo(String siralNo) {
		this.siralNo = siralNo;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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
