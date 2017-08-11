package com.it.netty.rpc.message;

import java.io.Serializable;
/**
 * 返回参数实体
 * @author 17070680
 * @param <T>
 *
 */
public class MsgResponse<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 120333333L;
	
	private String siralNo;
	
	private T data;
	

	
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getSiralNo() {
		return siralNo;
	}
	public void setSiralNo(String siralNo) {
		this.siralNo = siralNo;
	}


}
