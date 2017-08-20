package com.it.netty.rpc.cache;

public interface Cache <T,V> {
		 T getCache(V v);
		 void addCache(V V,T t);
}
