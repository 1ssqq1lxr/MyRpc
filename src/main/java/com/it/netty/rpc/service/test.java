package com.it.netty.rpc.service;

import java.lang.reflect.Method;

import com.it.netty.rpc.proxy.RpcProxyClient;

public class test {
	public static void main(String[] args) {
		PersonService personService=new RpcProxyClient().getProxy(PersonService.class);
		personService.setName("123");
		
		
	}
}
