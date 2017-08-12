package com.it.netty.rpc.framework;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NameSpaceHandlerXml extends NamespaceHandlerSupport {

	public void init() {
		// TODO Auto-generated method stub
		registerBeanDefinitionParser("service", new HandlerXml());
	}


}
