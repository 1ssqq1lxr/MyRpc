package com.it.netty.rpc.framework;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.it.netty.rpc.filter.AbatractParameterFilter;
import com.it.netty.rpc.message.Invocation;
import com.it.netty.rpc.proxy.cglib.RpcCglibProxyClient;

public class SpringConsumeBean implements FactoryBean, InitializingBean, DisposableBean {
	
	public Class<?> getClasst() {
		return classt;
	}

	public void setClasst(Class<?> classt) {
		this.classt = classt;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public SpringConsumeBean() {
		super();
	}
	private AbatractParameterFilter<Invocation> filter;
	private Class<?> classt;
	private String className;
	private Object object ;
	
	
	public AbatractParameterFilter<Invocation> getFilter() {
		return filter;
	}

	public void setFilter(AbatractParameterFilter<Invocation> filter) {
		this.filter = filter;
	}

	public SpringConsumeBean(Class<?> classt, String className,AbatractParameterFilter<Invocation> filter) {
		super();
		this.classt = classt;
		this.className = className;
		this.filter=filter;
	}

	private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();


	public  Object getObject() throws Exception {
		// TODO Auto-generated method stub
	
		return object;
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return object==null?Object.class:object.getClass();
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		map.clear();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		object=map.get(className);		
		if(object==null){
			object = this.filter.getDefaultProxy().getProxy(classt,this.filter);
		}
	}



}
