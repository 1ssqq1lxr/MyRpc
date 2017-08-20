package com.it.netty.rpc.cache;

import java.util.concurrent.ConcurrentHashMap;

public  class CacheFactory <T,V> implements Cache <T,V>{
	private  ConcurrentHashMap<V, T> commons = new ConcurrentHashMap<V,T>();

	@SuppressWarnings("unchecked")
	@Override
	public T getCache(V str) {
		// TODO Auto-generated method stub
		return   (T) commons.get(str);
	}

	@Override
	public  void addCache(V str,T t) {
		// TODO Auto-generated method stub
		commons.put(str, t);
	}
	
	
}
