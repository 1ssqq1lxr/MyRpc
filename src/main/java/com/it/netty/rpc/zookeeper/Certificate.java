package com.it.netty.rpc.zookeeper;
/**
 * 认证凭证
 * @author 17070680
 *
 */
public class Certificate {

	private String scheme="digest";
	private String username="admin";
	private String password ="admin";
	
	
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return this.username+":"+this.password;
	}

}
