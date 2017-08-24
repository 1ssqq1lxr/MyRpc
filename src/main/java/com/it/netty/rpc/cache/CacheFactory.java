package com.it.netty.rpc.cache;

import java.util.concurrent.ConcurrentHashMap;

public  class CacheFactory <K,V> implements Cache <K,V>{
	private  ConcurrentHashMap<K, V> commons = new ConcurrentHashMap<K,V>();

	@SuppressWarnings("unchecked")
	@Override
	public V getCache(K k) {
		// TODO Auto-generated method stub
		return   (V) commons.get(k);
	}

	@Override
	public  void addCache(K k,V v) {
		// TODO Auto-generated method stub
		commons.put(k, v);
	}
	@Override
	public  void putIfAbsentCache(K k,V v) {
		// TODO Auto-generated method stub
		commons.putIfAbsent(k, v);
	}
	@Override
	public  void remove(K k) {
		// TODO Auto-generated method stub
		commons.remove(k);
	}


	
}
