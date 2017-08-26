package com.it.netty.rpc.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.it.netty.rpc.framework.FrameworkRpcParseUtil.ComponentCallback;
import com.it.netty.rpc.romote.DeafultNettyServerRemoteConnection;
import com.it.netty.rpc.service.ServiceObjectFind;
import com.it.netty.rpc.zookeeper.Certificate;
import com.it.netty.rpc.zookeeper.ZookeeperService;


public class HandlerService extends AbstractSingleBeanDefinitionParser {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	ConcurrentHashSet<String> registClassNames  = new ConcurrentHashSet<>();
	private final String DEFAULT_NETTY_SERVICEOBJECTFINDINTEFERCE="serviceObjectFindInteferce";
	private final String DEFAULT_NETTY_NAME="default_server_tcp";
	private final String DEFAULT_ZOOKEEPER_NAME="default_server_zookeeper";
	private final String DEFAULT_ZOOKEEPER_PATH="rpc";
	@Override
	protected Class<?> getBeanClass(Element element) {
		// TODO Auto-generated method stub
		return ZkBeanService.class;
	}


	@Override
	protected void doParse(final Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {
		// TODO Auto-generated method stub
		try {
			FrameworkRpcParseUtil.parse(DEFAULT_NETTY_SERVICEOBJECTFINDINTEFERCE, ServiceObjectFind.class, element, parserContext);
			FrameworkRpcParseUtil.parse(DEFAULT_NETTY_NAME, DeafultNettyServerRemoteConnection.class, element, parserContext,new ComponentCallback() {
				@Override
				public void onParse(RootBeanDefinition beanDefinition) {
					// TODO Auto-generated method stub
					beanDefinition.getPropertyValues().addPropertyValue("port", element.getAttribute("serverPort"));
					beanDefinition.getPropertyValues().addPropertyValue(DEFAULT_NETTY_SERVICEOBJECTFINDINTEFERCE, new RuntimeBeanReference(DEFAULT_NETTY_SERVICEOBJECTFINDINTEFERCE));
				}
			});
			NodeList serviceRegeist = element.getElementsByTagName("rpc:serviceRegeist"); //开启zk
			FrameworkRpcParseUtil.parse(DEFAULT_ZOOKEEPER_NAME, ZookeeperService.class, element, parserContext,new ComponentCallback() {
				@Override
				public void onParse(RootBeanDefinition beanDefinition) {
					// TODO Auto-generated method stub
					beanDefinition.getPropertyValues().addPropertyValue("port", element.getAttribute("serverPort"));
					beanDefinition.getPropertyValues().addPropertyValue("zkAddress", element.getAttribute("zkAddress"));
					beanDefinition.getPropertyValues().addPropertyValue("certificate",new Certificate());
					beanDefinition.getPropertyValues().addPropertyValue("path",DEFAULT_ZOOKEEPER_PATH);
					beanDefinition.getPropertyValues().addPropertyValue("path",DEFAULT_ZOOKEEPER_PATH);
				}
			}); // 开启tcp服务端
			for(int i=0;i<serviceRegeist.getLength();i++){ // 注册服务
				Element item = (Element) serviceRegeist.item(i);
				registClassNames.add(item.getAttribute("class"));
			}
			builder.addPropertyValue("registClassNames", registClassNames);
			builder.addPropertyValue("zookeeperService", new RuntimeBeanReference(DEFAULT_ZOOKEEPER_NAME) );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(this.getClass().getName()+"error regeist service {}" +e);
		}
		

		
	}
}
