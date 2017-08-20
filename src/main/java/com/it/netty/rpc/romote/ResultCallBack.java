package com.it.netty.rpc.romote;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.message.Const;
import com.it.netty.rpc.message.Result;


public class ResultCallBack implements Callback{
	private long timeout; //超时时间
	
	
	
	public ResultCallBack(long timeout) {
		super();
		this.timeout = timeout;
	}
	private static final Logger log = LoggerFactory.getLogger(ResultCallBack.class.getSimpleName());
	Result result;
	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	
	public void putResult(Result result){
		try {
			if(result==null){
				lock.lock();
				this.result=result; 
				condition.notifyAll();
			}
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		finally{
			lock.unlock();	
		}
	}
	@Override
	public Result getObject() {
		// TODO Auto-generated method stub
		try {
			if(result==null){
				lock.lock();
				condition.await(timeout, TimeUnit.MILLISECONDS);
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return new Result(null, new RuntimeException(""), "请求超市", Const.ERROR_CODE);
		}
		finally{
			lock.unlock();	
		}
		return null;
	}

	
	
}
