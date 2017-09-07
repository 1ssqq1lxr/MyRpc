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
		person.setName1("123");
		person.setName2("123");
		person.setName3("123");
		person.setName4("123");
		person.setName5("123");
		person.setName6("123");
		person.setName7("123");
		person.setName8("123");
		person.setName9("123");
		person.setName10("123");
		person.setName11("123");
		person.setName12("123");
		person.setName13("123");
		person.setName14("123");
		person.setName15("123");
		person.setName16("123");
		person.setName17("123");
		person.setName18("123");
		person.setName19("123");
		person.setName20("123");
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
