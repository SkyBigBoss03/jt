package com.jt.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jt.manage.mapper.UserMapper;
import com.jt.manage.pojo.User;
@Service
public class UserServiceImpl implements UserService {
	
	/**
	 * UserMapper是一个接口,接口不能直接实例化,
	 * spring可以为其创建代理对象,之后通过@Autowired实现
	 * 自动的依赖注入
	 */
	@Autowired
	private UserMapper userMapper;
	
	//表示该方法需要添加事务
	@Override
	//@Transactional(propagation=Propagation.REQUIRED)
	public List<User> findAll() {
		
		System.out.println("获取代理对象的类型:"+userMapper.getClass());
		return userMapper.findAll();
	}
	
}
