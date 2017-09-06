package com.it.netty.rpc.service;

import com.it.netty.rpc.annocation.RpcService;

@RpcService
public class PersonServiceImpl implements PersonService{

	@Override
	public Person getName() {
		// TODO Auto-generated method stub
		Person person = new Person();
		person.setAge(13);
		person.setName("lisi");
		return person;
	}

	@Override
	public String setName(String name) {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void get1Name() {
		// TODO Auto-generated method stub
		System.out.println("kkkkkk");
	}

}
