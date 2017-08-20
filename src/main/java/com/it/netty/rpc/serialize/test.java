package com.it.netty.rpc.serialize;

public class test {
	public static void main(String[] args) {
		int value = SerializeEnum.JDKSERIALIZE.getValue();
		SerializeEnum valueOf = SerializeEnum.valueOf("MARSHELLINGSERIALIZE");
		int value2 = valueOf.getValue();
		System.out.println(value);
		System.out.println(value2);
	}
}
