package com.it.netty.rpc.romote;

public class TestServer {
	public static void main(String[] args) {
		DeafultNettyServerRemoteConnection connection = new DeafultNettyServerRemoteConnection(8099);
		connection.start();
	}
}
