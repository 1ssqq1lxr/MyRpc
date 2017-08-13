package com.it.netty.rpc.framework;

import java.util.Set;

public class ZkBean {
	private String address;
	private String protocol;
	private Set<String>  classes;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public Set<String> getClasses() {
		return classes;
	}
	public void setClasses(Set<String> classes) {
		this.classes = classes;
	}
	
}
