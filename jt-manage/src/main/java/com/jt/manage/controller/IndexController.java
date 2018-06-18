package com.jt.manage.controller;

import java.io.Serializable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController implements Serializable{


	//实现系统首页的跳转
	@RequestMapping("/index")
	public String index(){
		
		return "index";
	}
	
	//要求:一个方法实现全部页面的跳转???
	/**
	 * 需求分析:
	 * 要求:一个方法实现全部页面的跳转???
	 * 分析:根据页面的请求,实现页面的跳转
	 * 	url /page/item-add   页面  item-add.jsp
	 * 说明:如果采用单个页面的跳转,则方法较多,比较繁琐
	 * 
	 * 解决:
	 * 	 思路:动态的获取用户的请求路径,之后实现页面的自动跳转.
	 * 	 笨方法:通过request对象获取请求路径之后截串,返回数据.
	 *   restFul格式:
	 *   
	 *   知识回顾:
	 *   1.get请求: localhost:8090/addUser?id=1&name=tom&age=18
	 *   2.restFul结构:
	 *   	格式要求:
	 *   		1.参数与参数之间使用"/"分割,
	 *   		2.参数的位置是固定的.
	 *   	    3.不需要传递参数名称
	 *     restFul格式:
	 *     		url:localhost:8090/addUser/1/tom/18
	 *     参数的接收:
	 *     		接收:/addUser/{id}/{name}/{age}
	 *     		参数使用{}进行包装,之后通过@PathVariable注解取值
	 *     		如果传递的参数名称和获取的参数名称是一致的则
	 *     		@PathVariable(value="age")中的value可以省略不写
	 */
	//@RequestMapping("/addUser/{id}/{name}/{age}")
	public String addUser(
			@PathVariable Integer id,
			@PathVariable String name,
			@PathVariable(value="age")Integer age){
		
		return "ok";	
	}
	
	
	//使用restful方法实现页面通用跳转
	@RequestMapping("/page/{module}")
	public String itemAdd(@PathVariable String module){
		
		return module;
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
