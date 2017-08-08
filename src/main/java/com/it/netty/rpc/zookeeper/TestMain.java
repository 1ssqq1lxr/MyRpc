package com.it.netty.rpc.zookeeper;

public class TestMain {
		public static void main(String[] args) {
			
			ServiceRegist serviceRegist = new ServiceRegist("localhost:12181");
			serviceRegist.register("127.0.0.1:8199");
		}
}
 