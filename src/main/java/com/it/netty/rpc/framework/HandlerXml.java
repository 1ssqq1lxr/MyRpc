package com.it.netty.rpc.framework;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;


public class HandlerXml extends AbstractSingleBeanDefinitionParser {
    protected Class getBeanClass(Element element) {  
        return Test.class;  
    }  
	@Override
	protected void doParse(Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {
		// TODO Auto-generated method stub
		String address = element.getAttribute("address");
		String protocol = element.getAttribute("protocol");
		builder.addPropertyValue("address", address);
		builder.addPropertyValue("protocol", protocol);
	}

}
