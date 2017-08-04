package com.it.netty.rpc.excutor;

import io.netty.util.concurrent.Future;

import java.util.concurrent.Callable;


/**
 * 线程池基类
 * @author 17070680
 *
 */
public interface AbstractExcutor {
	
	public void excute(Runnable runnable);
	public <T>	Future<T> submit(Callable<T> runnable);
}
