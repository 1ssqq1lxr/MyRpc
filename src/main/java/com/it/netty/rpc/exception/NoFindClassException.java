package com.it.netty.rpc.exception;

public class NoFindClassException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public NoFindClassException(String name){
		super(name);
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
	
}
