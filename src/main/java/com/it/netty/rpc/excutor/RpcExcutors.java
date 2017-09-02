package com.it.netty.rpc.excutor;

import io.netty.util.concurrent.Future;

import java.util.concurrent.Callable;

/**
 * 线程池实现类
 * @author 17070680
 *
 */
public class RpcExcutors extends AbstractRpcExcutors{
	public RpcExcutors(String name) {
		super(name);
	}
	public RpcExcutors(String name, int corePoolSize,int intmaximumPoolSize) {
		super(name,corePoolSize,intmaximumPoolSize);
	}
	public void excute(Runnable runnable) {
		// TODO Auto-generated method stub
		service.execute(runnable);
	}
	public <T> Future<T>  submit(Callable<T> runnable) {
		// TODO Auto-generated method stub
		return  (Future<T>) service.submit(runnable);
	}

}
