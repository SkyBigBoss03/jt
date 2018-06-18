package com.jt.dubbox.service.impl;

import com.jt.dubbox.api.IProcessData;

public class ProcessDataImpl implements IProcessData {
	
	public String hello(String name) {
		//System.out.println("我是服务端程序,通过接口调用成功");
		return "service1 hello : " + name;
	}
}