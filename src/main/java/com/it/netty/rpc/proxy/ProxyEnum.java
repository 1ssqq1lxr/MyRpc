package com.it.netty.rpc.proxy;

import com.it.netty.rpc.proxy.cglib.RpcCglibProxyClient;
import com.it.netty.rpc.proxy.jdk.RpcJdkProxyClient;


public enum ProxyEnum {
		JDK("jdk"),
		CGLIB("cglib");
		public Proxy proxy;
		ProxyEnum(String proxyName){
			switch (proxyName) {
			case "jdk":
				this.proxy= new RpcJdkProxyClient();
				break;
			case "cglib":
				this.proxy= new RpcCglibProxyClient();
				break;
			}
		};
		public Proxy getProxy(){
			return proxy;
		}
}
