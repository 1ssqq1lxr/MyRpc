package com.it.netty.rpc.excutor;

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.LoggerFactory;
/**
 * 自定义线程池拒绝策略
 * @author 17070680
 *
 */
public class RpcRejectedExecution extends ThreadPoolExecutor.DiscardPolicy{
	protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		// TODO Auto-generated method stub
		logger.info(getClass().getName()+":线程池已满,{}线程遭到丢弃",r.toString());
		super.rejectedExecution(r, e);
	}
	
}
