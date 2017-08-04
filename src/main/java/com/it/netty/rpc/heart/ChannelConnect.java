package com.it.netty.rpc.heart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.core.RpcClientInit;
/**
 * 断电重连 如果一个线程连接成功后 自动关闭所有线程
 * @author 17070680
 *
 */
public class ChannelConnect implements Runnable{
	RpcClientInit client ;
	
  	public ChannelConnect(RpcClientInit client) {
		super();
		this.client = client;

	}

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public void run() {
		// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			try {
				// TODO Auto-generated method stub
				Thread.sleep(20000);
				logger.info("重连中");
				client.connect();
				
			} catch (Exception e) {
				// TODO: handle exception
				logger.error( e.getMessage());
			}
	}

}
