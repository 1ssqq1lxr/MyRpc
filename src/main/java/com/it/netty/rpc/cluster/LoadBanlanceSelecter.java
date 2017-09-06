package com.it.netty.rpc.cluster;


public class LoadBanlanceSelecter {
	public  LoadBanlance selectLoadBanlance(String loadBanlance){
		switch (loadBanlance) {
		case "random":
			return 	LoadBanlanceEnum.RANDOM.getLoadBanlance();
		case "roundRobin":
			return 	LoadBanlanceEnum.ROUNDROBIN.getLoadBanlance();
		case "ip":
			return 	LoadBanlanceEnum.IPRANDOM.getLoadBanlance();
		default:
			return 	LoadBanlanceEnum.RANDOM.getLoadBanlance();
		}
	} 
}
