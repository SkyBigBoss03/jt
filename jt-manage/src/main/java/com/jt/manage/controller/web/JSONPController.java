package com.jt.manage.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JSONPController {
	
	//http://manage.jt.com/web/testJSONP
	//返回数据  必须满足JSONP要求  function(JSON)
	@RequestMapping("/web/testJSONP")
	@ResponseBody
	public String testJSONP(String callback){
		
		String json = "{\"id\":\"100\",\"name\":\"tom\"}";
		return callback+"("+json+")";		
	}
}
