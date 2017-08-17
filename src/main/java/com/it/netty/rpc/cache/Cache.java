package com.it.netty.rpc.cache;

public interface Cache {
		<T> T getCache(String str);
		<T> void addCache(String str,T t);
}
