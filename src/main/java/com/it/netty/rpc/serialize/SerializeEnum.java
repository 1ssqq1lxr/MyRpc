package com.it.netty.rpc.serialize;

import com.it.netty.rpc.serialize.java.ByteObjConverter;

public enum SerializeEnum {
		JDKSERIALIZE(1),
		PROTOBUFSERIALIZE(2),
		MARSHELLINGSERIALIZE(3);
		private int value;
		SerializeEnum(int value){
				this.value=value;
		}
		
		public BaseRpcSerialize getSerialBean(){
			switch (value) {
			case 1:
				return  new ByteObjConverter();

			default:
				break;
			}
			return null;
		}
}
