package com.it.netty.rpc.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/** 
 * 客户端加载器 单例
 * @author 17070680
 *
 */
public class RpcLoader {
		private volatile  static RpcLoader rpcLoader;
		private RpcClientInit init;
		private ChannelFuture start ;
		private static Object obj = new Object();
		private RpcLoader (){
			init=new RpcClientInit();
			start= init.start();
		}
		public static RpcLoader getloader(){ 
			if(rpcLoader==null){
				synchronized (obj) {
					if(rpcLoader==null){
						rpcLoader = new RpcLoader();
						obj.notifyAll();
						return rpcLoader;
					}
				}
			}
			return rpcLoader;
		}
		
		public RpcClientInit getRpcClientInit(){
			if(init==null){
				synchronized (obj) {
					if(init==null){
						try {
							obj.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			return init;
		}
		
		public Channel getChannel(){
			if(start.isSuccess()){
				return start.channel();
			}
			return null;
		}
		
		
		
}
