package com.it.netty.rpc.zookeeper;

import java.io.Serializable;

import com.it.netty.rpc.message.URI;

public class RemoteAddress implements Serializable{
		private static final long serialVersionUID = 122L;
		private String nodeName;
		private URI uri;
		public String getNodeName() {
			return nodeName;
		}
		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}
		public URI getUri() {
			return uri;
		}
		public void setUri(URI uri) {
			this.uri = uri;
		}
		public RemoteAddress(String nodeName, URI uri) {
			this.nodeName = nodeName;
			this.uri = uri;
		}
		public RemoteAddress() {
			
		}
		
		
}
