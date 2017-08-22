package com.it.netty.rpc.serialize;

public class test {
	public static void main(String[] args) {
		int value = SerializeEnum.JDKSERIALIZE.getValue();
		SerializeEnum valueOf = SerializeEnum.valueOf("MARSHELLINGSERIALIZE");
		int value2 = valueOf.getValue();
		System.out.println(value);
		System.out.println(value2);
		 int TOP_LENGTH=129>>1|34;
		System.out.println((byte)TOP_LENGTH);
	}
}
