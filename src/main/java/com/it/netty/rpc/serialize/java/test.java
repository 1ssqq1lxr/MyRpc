package com.it.netty.rpc.serialize.java;

import com.it.netty.rpc.proxy.RpcProxy;
import com.it.netty.rpc.serialize.BaseRpcSerialize;

public class test {
	
	public static void main(String[] args) {
		String strName = "";
        int length = strName.length();
        StringBuilder stringBuilder = new StringBuilder();
        if (length >3){
            stringBuilder.append(strName.substring(0, 2));
            for (int i = 0; i < length - 2; i++) {
                stringBuilder.append("*");
            }
        }else {
            stringBuilder.append(strName.charAt(0));
            for (int i = 0; i < length - 1; i++) {
                stringBuilder.append("*");
            }
        }
        System.out.println(stringBuilder.toString());
	}

}
