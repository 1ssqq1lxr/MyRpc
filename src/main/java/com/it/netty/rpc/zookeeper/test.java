package com.it.netty.rpc.zookeeper;

public class test {
			public static void main(String[] args) {
				get("lisi");
				get("zhangdan");
			}
			private static  void get(final String name) {
				// TODO Auto-generated method stub
				System.out.println(name);
			}
}
