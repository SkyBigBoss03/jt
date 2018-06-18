package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.manage.pojo.User;
import com.jt.manage.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/findAll")
	public String findAll(Model model){
		List<User> userList = userService.findAll();
		//需要将数据传递到页面中  将数据保存到request域中
		model.addAttribute("userList", userList);	
		//返回展现的页面
		return "userList";
		
	}
}
