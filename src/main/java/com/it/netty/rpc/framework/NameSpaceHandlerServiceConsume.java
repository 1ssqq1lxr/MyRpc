package com.it.netty.rpc.framework;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NameSpaceHandlerServiceConsume extends NamespaceHandlerSupport {

	public void init() {
		// TODO Auto-generated method stub
		registerBeanDefinitionParser("serviceBind", new HandlerServiceConsume());
	}


}
