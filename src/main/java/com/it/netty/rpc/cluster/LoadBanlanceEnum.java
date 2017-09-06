package com.it.netty.rpc.cluster;



public enum LoadBanlanceEnum {
	RANDOM("random"),
	ROUNDROBIN("roundRobin"),
	IPRANDOM("ip");
	private LoadBanlance loadBanlance;
	LoadBanlanceEnum(String loadbanlanceName){
		switch (loadbanlanceName) {
		case "random":
			this.loadBanlance= new RandomLoadBanlance();
			break;
		case "roundRobin":
			this.loadBanlance= new RoundRobinLoadBanlance();
			break;
		case "ip":
			this.loadBanlance= new RoundRobinLoadBanlance();
			break;
		}
	};
	public LoadBanlance getLoadBanlance(){
		return loadBanlance;
	}
}
