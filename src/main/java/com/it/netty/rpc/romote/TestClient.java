package com.it.netty.rpc.romote;

import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.message.Result;
import com.it.netty.rpc.message.URI;

public class TestClient {
	static DeafultNettyClientRemoteConnection s=DeafultNettyClientRemoteConnection.newInstance();
	
	public static void main(String[] args) {
		Invocation invocation = new Invocation();
		invocation.setProtocol("HESSIAN");
		invocation.setTimeout(2000);
		URI uri = new URI();
		uri.setHost("127.0.0.1");
		uri.setPort(8099);
		invocation.setUri(uri );;
		Callback invokeAsync = s.invokeAsync(invocation);
		Result  object = invokeAsync.getObject();
		System.out.println(object.getResultCode());
	}
}
