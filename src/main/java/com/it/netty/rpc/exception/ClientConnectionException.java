package com.it.netty.rpc.exception;

public class ClientConnectionException extends RuntimeException{
	
	public ClientConnectionException(String name){
		super(name);
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
	
}
