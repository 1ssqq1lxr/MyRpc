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
import com.it.netty.rpc.filter.ParameterFilter;
import com.it.netty.rpc.framework.FrameworkRpcParseUtil.ComponentCallback;
import com.it.netty.rpc.zookeeper.Certificate;
import com.it.netty.rpc.zookeeper.ZookeeperService;


public class HandlerServiceConsume extends AbstractSingleBeanDefinitionParser {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private final String DEFAULT_ZOOKEEPER_NAME="default_client_zookeeper";
	private final String DEFAULT_ZOOKEEPER_PATH="rpc";
	private final String DEFAULT_ZOOKEEPER_ZKADDRESS="zkAddress";
	private final String DEFAULT_ZOOKEEPER_PROTOCOL="protocol";
	private final String DEFAULT_ZOOKEEPER_SERVICECONSUME="rpc:serviceConsume";
	private final String DEFAULT_ZOOKEEPER_CLASS="interface";
	private final String DEFAULT_ZOOKEEPER_SERVER_NAME="name";
	private final String DEFAULT_PARAMETER_FILTER="filter";
	private final String DEFAULT_PARAMETER_PROXY="proxy";
	private final String DEFAULT_PARAMETER_LOADBANLANCE="loadBanlance";
	private final String DEFAULT_CLIENTGROUP_THREAD_NUMS = "clientGroup-thread-nums";

	private  ConcurrentHashSet<String> getClassNames  = new ConcurrentHashSet<>();
	@Override
	protected Class<?> getBeanClass(Element element) {
		return ZkBeanServiceConsume.class;
	}
	@Override
	protected void doParse(final Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {
		final String address = element.getAttribute(DEFAULT_ZOOKEEPER_ZKADDRESS);
		final String protocol = element.getAttribute(DEFAULT_ZOOKEEPER_PROTOCOL);
		final String loadBanlance = element.getAttribute(DEFAULT_PARAMETER_LOADBANLANCE);
		final String clientGroup_thread_nums = element.getAttribute(DEFAULT_CLIENTGROUP_THREAD_NUMS);
		builder.addPropertyValue("zkAddress", address);
		builder.addPropertyValue("protocol", protocol);
		NodeList serviceRegeist = element.getElementsByTagName(DEFAULT_ZOOKEEPER_SERVICECONSUME);
		FrameworkRpcParseUtil.parse(DEFAULT_ZOOKEEPER_NAME, ZookeeperService.class, element, parserContext,new ComponentCallback() {
			@Override
			public void onParse(RootBeanDefinition beanDefinition) {
				beanDefinition.getPropertyValues().addPropertyValue("zkAddress", element.getAttribute("zkAddress"));
				beanDefinition.getPropertyValues().addPropertyValue("certificate",new Certificate());
				beanDefinition.getPropertyValues().addPropertyValue("path",DEFAULT_ZOOKEEPER_PATH);
			}
		});
		FrameworkRpcParseUtil.parse(DEFAULT_PARAMETER_FILTER, ParameterFilter.class,element, parserContext,new ComponentCallback() {
			@Override
			public void onParse(RootBeanDefinition beanDefinition) {
				beanDefinition.getPropertyValues().addPropertyValue("loadBanlance",loadBanlance);
				beanDefinition.getPropertyValues().addPropertyValue("protocol",protocol);
				beanDefinition.getPropertyValues().addPropertyValue("clientGroup_thread_nums",clientGroup_thread_nums);
				beanDefinition.getPropertyValues().addPropertyValue("proxy",element.getAttribute(DEFAULT_PARAMETER_PROXY));
			}});
		for(int i=0;i<serviceRegeist.getLength();i++){ // 获取服务信息
			Element item = (Element) serviceRegeist.item(i);
			final String className = item.getAttribute(DEFAULT_ZOOKEEPER_CLASS);
			String name = item.getAttribute(DEFAULT_ZOOKEEPER_SERVER_NAME);
			try {
				final Class<?> loadClass = this.getClass().getClassLoader().loadClass(className);
				FrameworkRpcParseUtil.parse(name, SpringConsumeBean.class, element, parserContext,new ComponentCallback() {
					@Override
					public void onParse(RootBeanDefinition beanDefinition) {
						beanDefinition.getPropertyValues().addPropertyValue("classt",loadClass);
						beanDefinition.getPropertyValues().addPropertyValue("className",className);
						beanDefinition.getPropertyValues().addPropertyValue("filter",new RuntimeBeanReference(DEFAULT_PARAMETER_FILTER));
					}
				});
				getClassNames.add(className);
			} catch (ClassNotFoundException e) {
				logger.error("not find class {},{}", className,e);
			}
		}
		builder.addPropertyValue("getClassNames", getClassNames);
		builder.addPropertyValue("zookeeperService", new RuntimeBeanReference(DEFAULT_ZOOKEEPER_NAME));
	}

}
