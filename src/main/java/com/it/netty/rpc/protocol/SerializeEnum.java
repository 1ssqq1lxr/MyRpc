package com.it.netty.rpc.protocol;



public enum SerializeEnum {
		DEFAULTSERIALIZE(2),
		JAVA(1),
		HESSIAN(2),
		JACKSON(3);
		private int value;
		SerializeEnum(int value){
				this.value=value;
		}
		public int getValue() {
			return value;
		}
}
