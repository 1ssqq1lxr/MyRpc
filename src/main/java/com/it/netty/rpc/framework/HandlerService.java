package com.it.netty.rpc.framework;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.it.netty.rpc.core.RpcServerInit;
import com.it.netty.rpc.framework.FrameworkRpcParseUtil.ComponentCallback;
import com.it.netty.rpc.message.URI;
import com.it.netty.rpc.serialize.SerializeEnum;


public class HandlerService extends AbstractSingleBeanDefinitionParser {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected Class<?> getBeanClass(Element element) {
		// TODO Auto-generated method stub
		return ZkBeanService.class;
	}


	@Override
	protected void doParse(final Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {
		// TODO Auto-generated method stub
		
		InetAddress localHost = null;
		try {
			int serverPort = Integer.parseInt(element.getAttribute("serverPort"));
			builder.addPropertyValue("address", element.getAttribute("zkAddress"));
			builder.addPropertyValue("protocol", element.getAttribute("protocol"));
			NodeList serviceRegeist = element.getElementsByTagName("rpc:serviceRegeist");
			FrameworkRpcParseUtil.parse("tcpService", RpcServerInit.class, element, parserContext,new ComponentCallback() {
				@Override
				public void onParse(RootBeanDefinition beanDefinition) {
					// TODO Auto-generated method stub
					beanDefinition.getPropertyValues().addPropertyValue("port", element.getAttribute("serverPort"));
				}
			}); // 开启tcp服务端
			localHost = Inet4Address.getLocalHost();
			String hostAddress = localHost.getHostAddress();
			Set<String> hashset = new HashSet<>();
			for(int i=0;i<serviceRegeist.getLength();i++){ // 注册服务
				URI uri = new URI();
				uri.setHost(hostAddress);
				uri.setPort(serverPort);
				uri.setSerialMethod(SerializeEnum.JDKSERIALIZE.toString());
				Element item = (Element) serviceRegeist.item(i);
				String classe = item.getAttribute("class");
				hashset.add(classe);
				logger.info(this.getClass().getName()+"success regeist service {}" ,classe);
			}
			builder.addPropertyValue("classes", hashset);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error(this.getClass().getName()+"error regeist service {}" +e);
		}
		

		
	}
}
