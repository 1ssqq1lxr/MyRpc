package com.it.netty.rpc.cache;

public interface Cache <T> {
		 T getCache(String str);
		 void addCache(String str,T t);
}
