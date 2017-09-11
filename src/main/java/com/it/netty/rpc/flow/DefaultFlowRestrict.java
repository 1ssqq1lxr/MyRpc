package com.it.netty.rpc.flow;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFlowRestrict implements FlowRestrict {	
		private  final  Logger logger = LoggerFactory.getLogger(getClass());
		private Semaphore semaphore=null;
		private long  DEFAULT_TIME=2000L;
		public DefaultFlowRestrict(int proptity){
			semaphore = new Semaphore(proptity);
		}
		@Override
		public boolean restrict() {
			try {
				return semaphore.tryAcquire(DEFAULT_TIME,TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				logger.warn("not acquire semaphore",e);
				return false;
			}
		}
		@Override
		public void doRelease() {
			semaphore.release();
		}
		
}
