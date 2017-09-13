package com.it.netty.rpc.excutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.parser.Feature;
/**
 * 初始化线程池类
 * @author 17070680
 *
 */
public abstract class AbstractRpcExcutors  implements AbstractExcutor{
	ExecutorService service;
	
	private int corePoolSize =250;
	private	int intmaximumPoolSize=400;
	
	public ExecutorService getService() {
		return service;
	}

	public void setService(ExecutorService service) {
		this.service = service;
	}

	public AbstractRpcExcutors( String name){
		service = new ThreadPoolExecutor(this.corePoolSize, this.intmaximumPoolSize, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(200),new RpcThreadFactory(name),new RpcRejectedExecution());
	}
	public AbstractRpcExcutors( String name, int corePoolSize,int intmaximumPoolSize){
		service = new ThreadPoolExecutor(corePoolSize, intmaximumPoolSize, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(200),new RpcThreadFactory(name),new RpcRejectedExecution());
	}
}
