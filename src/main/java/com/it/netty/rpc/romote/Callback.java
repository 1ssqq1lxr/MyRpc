package com.it.netty.rpc.romote;

import com.it.netty.rpc.message.Result;
/**
 * 
 * @author 17070680
 *
 */
public interface Callback {
		void putResult(Result result);
		Object getObject();
}
