package com.it.netty.rpc.cluster;

import java.util.Random;

import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.zookeeper.RemoteAddress;

public class RandomLoadBanlance implements LoadBanlance{

	@Override
	public URI selectRandom(RemoteAddress[] uris) {
		// TODO Auto-generated method stub
		if(uris!=null&&uris.length>0){
			int random = uris.length;
			int nextInt = new Random().nextInt(random);
			return uris[nextInt].getUri();
		}
		return null;
	}

}
