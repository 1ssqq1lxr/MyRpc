package com.it.netty.rpc.framework;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NameSpaceHandlerService extends NamespaceHandlerSupport {
	public void init() {
		// TODO Auto-generated method stub
		registerBeanDefinitionParser("server", new HandlerService());
		registerBeanDefinitionParser("serviceBind", new HandlerServiceConsume());
	}
}
