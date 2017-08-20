package com.it.netty.rpc.romote;

import com.it.netty.rpc.message.Result;

public interface Callback {
		void putResult(Result result);
		Object getObject();
}
