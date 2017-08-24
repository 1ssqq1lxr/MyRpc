package com.it.netty.rpc;

import com.it.netty.rpc.cache.Cache;
import com.it.netty.rpc.cache.CacheFactory;
import com.it.netty.rpc.message.URI;

public class Config {
		public  static Cache<String,URI>  uri = new CacheFactory<>(); // channel
		
		public static String protocol;
		
		public static String zkAddress;
		
		public static String zkUserName;
		
		public static String ZkPassword;
		
		public static String schema;
		
		public static int rpcPort;
}
