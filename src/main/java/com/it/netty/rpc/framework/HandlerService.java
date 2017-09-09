package com.it.netty.rpc.framework;

import java.util.concurrent.ConcurrentHashMap;

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
import com.it.netty.rpc.service.FrameworkServiceObjectFind;
import com.it.netty.rpc.zookeeper.Certificate;
import com.it.netty.rpc.zookeeper.ZookeeperService;


public class HandlerService extends AbstractSingleBeanDefinitionParser {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	public  final ConcurrentHashSet<String> registClassNames  = new ConcurrentHashSet<>();
	private final String DEFAULT_NETTY_SERVICEOBJECTFINDINTEFERCE="serviceObjectFindInteferce";
	private final String DEFAULT_NETTY_NAME="default_server_tcp";
	private final String DEFAULT_ZOOKEEPER_NAME="default_server_zookeeper";
	private final String DEFAULT_ZOOKEEPER_PATH="rpc";
	private final String DEFAULT_TIMEOUT="timeout";
	public static final ConcurrentHashMap<String, Long> timeouts = new ConcurrentHashMap<String, Long>();
	@Override
	protected Class<?> getBeanClass(Element element) {
		return ZkBeanService.class;
	}


	@Override
	protected void doParse(final Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {
		try {
 			FrameworkRpcParseUtil.parse(DEFAULT_NETTY_SERVICEOBJECTFINDINTEFERCE, FrameworkServiceObjectFind.class, element, parserContext);
			FrameworkRpcParseUtil.parse(DEFAULT_NETTY_NAME, DeafultNettyServerRemoteConnection.class, element, parserContext,new ComponentCallback() {
				@Override
				public void onParse(RootBeanDefinition beanDefinition) {
					beanDefinition.getPropertyValues().addPropertyValue("port", element.getAttribute("serverPort"));
					beanDefinition.getPropertyValues().addPropertyValue("serviceObjectFindInteferce", new RuntimeBeanReference(DEFAULT_NETTY_SERVICEOBJECTFINDINTEFERCE));
				}
			});
			NodeList serviceRegeist = element.getElementsByTagName("rpc:serviceRegeist"); //开启zk
			FrameworkRpcParseUtil.parse(DEFAULT_ZOOKEEPER_NAME, ZookeeperService.class, element, parserContext,new ComponentCallback() {
				@Override
				public void onParse(RootBeanDefinition beanDefinition) {
					beanDefinition.getPropertyValues().addPropertyValue("port", element.getAttribute("serverPort"));
					beanDefinition.getPropertyValues().addPropertyValue("zkAddress", element.getAttribute("zkAddress"));
					beanDefinition.getPropertyValues().addPropertyValue("certificate",new Certificate());
					beanDefinition.getPropertyValues().addPropertyValue("path",DEFAULT_ZOOKEEPER_PATH);
				}
			}); 
			for(int i=0;i<serviceRegeist.getLength();i++){ // 注册服务
				Element item = (Element) serviceRegeist.item(i);
				String attribute = item.getAttribute("class");
				String timeout=item.getAttribute(DEFAULT_TIMEOUT);
				registClassNames.add(attribute);
				if(timeout!="" && timeout!=null)
				HandlerService.timeouts.putIfAbsent(attribute, Long.parseLong(timeout));
			}
			builder.addPropertyValue("registClassNames", registClassNames);
			builder.addPropertyValue("zookeeperService", new RuntimeBeanReference(DEFAULT_ZOOKEEPER_NAME) );
		} catch (Exception e) {
			logger.error(this.getClass().getName()+"error regeist service {}" +e);
		}
		

		
	}
}
