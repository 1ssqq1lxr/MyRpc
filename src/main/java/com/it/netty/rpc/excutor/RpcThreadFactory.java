package com.it.netty.rpc.excutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;
/**
 * 自定义ThreadFactory
 * @author 17070680
 *
 */
public class RpcThreadFactory implements ThreadFactory {
	
	String name;
	protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	public RpcThreadFactory(String name) {
		super();
		this.name = name;
	}
	AtomicInteger num = new AtomicInteger(0);
	public Thread newThread(Runnable runnable) {

		// TODO Auto-generated method stub
		logger.info(this+":create name:{}-{}线程成功",name,	num.incrementAndGet());
		String newname =name+"-"+num.intValue();
		Thread result=new Thread(runnable,newname);    
	
		return result;  
	}
	

}
