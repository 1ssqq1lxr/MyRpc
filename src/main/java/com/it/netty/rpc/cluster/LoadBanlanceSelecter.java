package com.it.netty.rpc.cluster;


public class LoadBanlanceSelecter {
	public static LoadBanlance selectLoadBanlance(String loadBanlance){
		switch (loadBanlance) {
		case "random":
			return 	LoadBanlanceEnum.RANDOM.getLoadBanlance();
		case "roundRobin":
			return 	LoadBanlanceEnum.ROUNDROBIN.getLoadBanlance();
		default:
			return 	LoadBanlanceEnum.RANDOM.getLoadBanlance();
		}
	} 
}
