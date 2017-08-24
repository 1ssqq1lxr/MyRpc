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
import com.it.netty.rpc.proxy.RpcProxyClient;
import com.it.netty.rpc.zookeeper.Certificate;
import com.it.netty.rpc.zookeeper.ZookeeperService;


public class HandlerServiceConsume extends AbstractSingleBeanDefinitionParser {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private final String DEFAULT_ZOOKEEPER_NAME="default_client_zookeeper";
	private final String DEFAULT_ZOOKEEPER_PATH="rpc";
	private  ConcurrentHashSet<String> getClassNames  = new ConcurrentHashSet<>();
	@Override
	protected Class<?> getBeanClass(Element element) {
		// TODO Auto-generated method stub
		return ZkBeanServiceConsume.class;
	}


	@Override
	protected void doParse(final Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {
		// TODO Auto-generated method stub
		String address = element.getAttribute("zkAddress");
		String protocol = element.getAttribute("protocol");
		builder.addPropertyValue("zkAddress", address);
		builder.addPropertyValue("protocol", protocol);
		NodeList serviceRegeist = element.getElementsByTagName("rpc:serviceConsume");
		FrameworkRpcParseUtil.parse(DEFAULT_ZOOKEEPER_NAME, ZookeeperService.class, element, parserContext,new ComponentCallback() {
			@Override
			public void onParse(RootBeanDefinition beanDefinition) {
				// TODO Auto-generated method stub
				beanDefinition.getPropertyValues().addPropertyValue("zkAddress", element.getAttribute("zkAddress"));
				beanDefinition.getPropertyValues().addPropertyValue("certificate",new Certificate());
				beanDefinition.getPropertyValues().addPropertyValue("path",DEFAULT_ZOOKEEPER_PATH);
			}
		});
		for(int i=0;i<serviceRegeist.getLength();i++){ // 获取服务信息
			Element item = (Element) serviceRegeist.item(i);
			String className = item.getAttribute("class");
			String name = item.getAttribute("name");
			try {
				Class<?> loadClass = this.getClass().getClassLoader().loadClass(className);
				Object proxy = RpcProxyClient.getProxy(loadClass);
				FrameworkRpcParseUtil.parse(name, proxy.getClass(), element, parserContext,new ComponentCallback() {
					@Override
					public void onParse(RootBeanDefinition beanDefinition) {
					}
				});
				getClassNames.add(className);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				logger.error("not find class {},{}", className,e);
			}
		}
		builder.addPropertyValue("getClassNames", getClassNames);
		builder.addPropertyValue("zookeeperService", new RuntimeBeanReference(DEFAULT_ZOOKEEPER_NAME));
	}
	
}
