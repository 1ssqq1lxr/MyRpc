package com.it.netty.rpc.serialize;

import java.io.Serializable;

public class MsgConventer implements Serializable{
	private int header =1;
	private int bodyLength;
	private Object obj;
	public int getHeader() {
		return header;
	}
	public void setHeader(int header) {
		this.header = header;
	}
	public int getBodyLength() {
		return bodyLength;
	}
	public void setBodyLength(int bodyLength) {
		this.bodyLength = bodyLength;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
