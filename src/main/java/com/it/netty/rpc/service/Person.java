package com.it.netty.rpc.service;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.it.netty.rpc.serialize.SerializeEnum;

public class Person implements Serializable{
		private String name;
		private	int age;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
}
