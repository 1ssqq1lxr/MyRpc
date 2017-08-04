package com.it.netty.rpc.message;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.LoggerFactory;

/**
 * 消息回调中间类
 * @author 17070680
 *
 */
public class MsgBackCall {
	protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	private MsgResponse  response = null;
	private Lock lock = new ReentrantLock();
	Condition condition=lock.newCondition();
	public Object call(){
			try {
				
				lock.lock();
				try {
					if(response==null)
					condition.await(2,TimeUnit.SECONDS);
					return response.getData();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.info(getClass()+"执行超时");
					return null;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(getClass()+":"+e.getMessage());
				return null;
			}
			finally{
				lock.unlock();
			}
	
	}
	public void putData(MsgResponse o){
		
		try {
			lock.lock();
			this.response = o;
			condition.signal();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(getClass()+":"+e.getMessage());
		}
		finally{
			lock.unlock();
		}
	
	}
	

}
