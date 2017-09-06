package com.it.netty.rpc.cluster;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.romote.NettyServerApiService;
import com.it.netty.rpc.zookeeper.RemoteAddress;

public class IpRandomLoadBanlance implements LoadBanlance{
	private static final Logger log = LoggerFactory.getLogger(IpRandomLoadBanlance.class.getSimpleName());
	String name ="ipRandom";
	public String getName() {
		return name;
	}
	@Override
	public URI selectRandom(RemoteAddress[] uris) {
		// TODO Auto-generated method stub
		if(uris!=null&&uris.length>0){
		try {
			String hostAddress = Inet4Address.getLocalHost().getHostAddress();
			Long hash = MurMurHash.hash(hostAddress);
			hash=Math.abs(hash);
			int index =(int) (hash==null?0:(hash%(uris.length)));
			return uris[index].getUri();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			log.info("acquire local ip error ", e);
		}
		}
		return null;
	}
}
