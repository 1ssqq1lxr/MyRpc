package com.it.netty.rpc.romote;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.message.Const;
import com.it.netty.rpc.message.Result;

/**
 * 
 * @author 17070680
 *
 */
public class ResultCallBack implements Callback{
	Thread thread;
	private String className; 
	private long timeout; //超时时间
	public ResultCallBack(long timeout,String className) {
		super();
		this.timeout = timeout;
		this.className = className;
	}
	private static final Logger log = LoggerFactory.getLogger(ResultCallBack.class.getSimpleName());
	Result result;
	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	public void putResult(Result result){
		try {
			if(this.result==null){
				lock.lock();
				this.result=result; 
				condition.signal();
			}
		} catch (Exception e) {
			log.error(this.getClass().getSimpleName()+"请求超时{}", e);
		}
		finally{
			lock.unlock();	
		}
	}
	@Override
	public Result getObject() {
		if(this.result==null){
			lock.lock();
			try{	
				boolean await = condition.await(timeout, TimeUnit.MILLISECONDS);
				if(await){
					return this.result;
				}
				else{
					log.error(className+"请求超时");
					return new Result(null, new RuntimeException(className+"请求超时"), "请求超时", Const.ERROR_CODE);
				}
					
			}
			catch (InterruptedException e) {
				log.error(this.getClass().getSimpleName()+"请求超时{}", e);
			}
			finally{
				lock.unlock();	
			}
		} 
		return result;
	}
}
