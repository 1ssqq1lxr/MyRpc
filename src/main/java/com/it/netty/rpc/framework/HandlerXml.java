package com.it.netty.rpc.framework;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


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
		NodeList elementsByTagName = element.getElementsByTagName("rpc:serviceRegeist");
		System.out.println(elementsByTagName.getLength());
		for(int i=0;i<elementsByTagName.getLength();i++){
			Element item = (Element) elementsByTagName.item(i);
			String name = item.getAttribute("name");
			String classe = item.getAttribute("class");
			System.out.println(name+":"+classe);
		}
		builder.addPropertyValue("address", address);
		builder.addPropertyValue("protocol", protocol);
	}

}
