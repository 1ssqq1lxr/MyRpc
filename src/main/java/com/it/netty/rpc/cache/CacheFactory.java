package com.it.netty.rpc.cache;

import java.util.concurrent.ConcurrentHashMap;

public  class CacheFactory implements Cache{
	private  ConcurrentHashMap<String, Object> commons = new ConcurrentHashMap<String,Object>();

	@Override
	public <T> T getCache(String str) {
		// TODO Auto-generated method stub
		return  (T) commons.get(str);
	}

	@Override
	public <T> void addCache(String str,T t) {
		// TODO Auto-generated method stub
		commons.put(str, t);
	}
	
	
}
