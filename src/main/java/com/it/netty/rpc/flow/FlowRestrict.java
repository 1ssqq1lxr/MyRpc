package com.it.netty.rpc.flow;

public interface FlowRestrict {
		boolean	restrict();
		void doRelease();
}
