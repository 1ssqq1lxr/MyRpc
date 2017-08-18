package com.it.netty.rpc.framework;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.serialize.SerializeEnum;
import com.it.netty.rpc.zookeeper.base.ServerInitialization;


public class HandlerServiceConsume extends AbstractSingleBeanDefinitionParser {
	private final  int port=8099;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected Class<?> getBeanClass(Element element) {
		// TODO Auto-generated method stub
		String namespaceURI = element.getNamespaceURI();
		String localName = element.getLocalName();
		Class<? extends Element> class1 = element.getClass();
		return ZkBeanService.class;
	}


	@Override
	protected void doParse(Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {
		// TODO Auto-generated method stub
		String address = element.getAttribute("address");
		String protocol = element.getAttribute("protocol");
		builder.addPropertyValue("address", address);
		builder.addPropertyValue("protocol", protocol);
		NodeList serviceRegeist = element.getElementsByTagName("rpc:serviceRegeist");
		InetAddress localHost = null;
		try {
			localHost = Inet4Address.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error(this.getClass().getName()+"error regeist service {}" +e);
		}
		String hostAddress = localHost.getHostAddress();
		ServerInitialization initialization = ServerInitialization.getInstance(address);
		Set<String> hashset = new HashSet<>();
		for(int i=0;i<serviceRegeist.getLength();i++){ // 注册服务
			URI uri = new URI();
			uri.setHost(hostAddress);
			uri.setPort(port);
			uri.setSerialMethod(SerializeEnum.JDKSERIALIZE.toString());
			Element item = (Element) serviceRegeist.item(i);
			String classe = item.getAttribute("class");
			hashset.add(classe);
			initialization.registURI(classe, uri);
			logger.info(this.getClass().getName()+"success regeist service {}" ,classe);
		}
		builder.addPropertyValue("classes", hashset);
	}
}
