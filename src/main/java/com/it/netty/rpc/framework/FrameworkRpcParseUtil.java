package com.it.netty.rpc.framework;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class FrameworkRpcParseUtil {
	 public static void parse(String id, Class<?> beanType, Element rootElement, ParserContext parserContext) {
	        try {
	            Object eleSource = parserContext.extractSource(rootElement);
	            RootBeanDefinition beanDefinition = new RootBeanDefinition();
	            beanDefinition.setSource(eleSource);
	            beanDefinition.setRole(BeanDefinition.ROLE_APPLICATION);
	            beanDefinition.setBeanClass(beanType);
	            beanDefinition.setLazyInit(false);
	            beanDefinition.setDependencyCheck(RootBeanDefinition.DEPENDENCY_CHECK_NONE);
	            beanDefinition.setAutowireCandidate(true);
	            beanDefinition.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
	            BeanDefinitionHolder beanholder = new BeanDefinitionHolder(beanDefinition, id);
	            BeanDefinitionReaderUtils.registerBeanDefinition(beanholder, parserContext.getRegistry());
	            BeanComponentDefinition componentDefinition = new BeanComponentDefinition(beanholder);
	            parserContext.registerComponent(componentDefinition);
	        } catch (Throwable e) {
	            parserContext.getReaderContext().error(e.getMessage(), rootElement);
	        }
	    }
	    public static String parse(Class<?> beanType, Element rootElement, ParserContext parserContext) {
	        String beanName = null;
	        try {
	            Object eleSource = parserContext.extractSource(rootElement);
	            RootBeanDefinition beanDefinition = new RootBeanDefinition();
	            beanDefinition.setSource(eleSource);
	            beanDefinition.setRole(BeanDefinition.ROLE_APPLICATION);
	            beanDefinition.setBeanClass(beanType);
	            beanDefinition.setLazyInit(false);
	            beanDefinition.setDependencyCheck(RootBeanDefinition.DEPENDENCY_CHECK_NONE);
	            beanDefinition.setAutowireCandidate(true);
	            beanDefinition.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
	            beanName = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, parserContext.getRegistry());
	            BeanDefinitionHolder beanholder = new BeanDefinitionHolder(beanDefinition, beanName);
	            BeanDefinitionReaderUtils.registerBeanDefinition(beanholder, parserContext.getRegistry());
	            BeanComponentDefinition componentDefinition = new BeanComponentDefinition(beanholder);
	            parserContext.registerComponent(componentDefinition);
	        } catch (Throwable e) {
	            parserContext.getReaderContext().error(e.getMessage(), rootElement);
	        }
	        return beanName;
	    }
	    
	    public static void parse(String id, Class<?> beanType, Element rootElement, ParserContext parserContext, ComponentCallback callback) {
	        try {
	            Object eleSource = parserContext.extractSource(rootElement);
	            RootBeanDefinition beanDefinition = new RootBeanDefinition();
	            beanDefinition.setSource(eleSource);
	            beanDefinition.setRole(BeanDefinition.ROLE_APPLICATION);
	            beanDefinition.setBeanClass(beanType);
	            beanDefinition.setLazyInit(false);
	            beanDefinition.setDependencyCheck(RootBeanDefinition.DEPENDENCY_CHECK_NONE);
	            beanDefinition.setAutowireCandidate(true);
	            beanDefinition.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
	            callback.onParse(beanDefinition);
	            BeanDefinitionHolder beanholder = new BeanDefinitionHolder(beanDefinition, id);
	            BeanDefinitionReaderUtils.registerBeanDefinition(beanholder, parserContext.getRegistry());
	            BeanComponentDefinition componentDefinition = new BeanComponentDefinition(beanholder);
	            parserContext.registerComponent(componentDefinition);
	        } catch (Throwable e) {
	            parserContext.getReaderContext().error(e.getMessage(), rootElement);
	        }
	    }
	    
	    public static String parse(Class<?> beanType, Element rootElement, ParserContext parserContext, ComponentCallback callback) {
	        String beanName = null;
	        try {
	            Object eleSource = parserContext.extractSource(rootElement);
	            RootBeanDefinition beanDefinition = new RootBeanDefinition();
	            beanDefinition.setSource(eleSource);
	            beanDefinition.setRole(BeanDefinition.ROLE_APPLICATION);
	            beanDefinition.setBeanClass(beanType);
	            beanDefinition.setLazyInit(false);
	            beanDefinition.setDependencyCheck(RootBeanDefinition.DEPENDENCY_CHECK_NONE);
	            beanDefinition.setAutowireCandidate(true);
	            beanDefinition.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
	            callback.onParse(beanDefinition);
	            beanName = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, parserContext.getRegistry());
	            BeanDefinitionHolder beanholder = new BeanDefinitionHolder(beanDefinition, beanName);
	            BeanDefinitionReaderUtils.registerBeanDefinition(beanholder, parserContext.getRegistry());
	            BeanComponentDefinition componentDefinition = new BeanComponentDefinition(beanholder);
	            parserContext.registerComponent(componentDefinition);
	        } catch (Throwable e) {
	            parserContext.getReaderContext().error(e.getMessage(), rootElement);
	        }
	        return beanName;
	    }
	    
	    public static interface ComponentCallback {
	        void onParse(RootBeanDefinition beanDefinition);
	    }
}
