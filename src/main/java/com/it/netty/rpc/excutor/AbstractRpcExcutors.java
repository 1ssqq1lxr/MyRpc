package com.it.netty.rpc.excutor;

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
	
	public ExecutorService getService() {
		return service;
	}

	public void setService(ExecutorService service) {
		this.service = service;
	}

	public AbstractRpcExcutors( String name){
		service = new ThreadPoolExecutor(10, 20, 2, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(),new RpcThreadFactory(name),new RpcRejectedExecution());
	}
}
