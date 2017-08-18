package com.it.netty.rpc.cache;

import java.util.concurrent.ConcurrentHashMap;

public  class CacheFactory <T> implements Cache <T>{
	private  ConcurrentHashMap<String, Object> commons = new ConcurrentHashMap<String,Object>();

	@SuppressWarnings("unchecked")
	@Override
	public T getCache(String str) {
		// TODO Auto-generated method stub
		return   (T) commons.get(str);
	}

	@Override
	public  void addCache(String str,T t) {
		// TODO Auto-generated method stub
		commons.put(str, t);
	}
	
	
}
