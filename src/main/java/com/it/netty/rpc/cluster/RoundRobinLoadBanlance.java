package com.it.netty.rpc.cluster;

import java.util.concurrent.atomic.AtomicInteger;

import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.zookeeper.RemoteAddress;

public class RoundRobinLoadBanlance implements LoadBanlance{
	private static final AtomicInteger index = new AtomicInteger(0);
	@Override
	public URI selectRandom(RemoteAddress[] uris) {
		// TODO Auto-generated method stub
		if(uris!=null&&uris.length>0){
			int random = uris.length;
			int incrementAndGet = getAndIncrement();
			int nextInt = incrementAndGet%random;
			return uris[nextInt].getUri();
		}
		return null;
	}
	public  final int getAndIncrement() {
		for (;;) {
			int current = index.get();
			int next = (current >= Integer.MAX_VALUE ? 0 : current + 1);
			if (index.compareAndSet(current, next)) {
				return current;
			}
		}
	}

}
