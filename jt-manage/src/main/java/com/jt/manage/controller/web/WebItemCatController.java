package com.jt.manage.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.service.ItemCatService;

@Controller
@RequestMapping("/web/itemcat")
public class WebItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	//实现前端商品分类目录的展现
	///all?callback=category.getDataService
	@RequestMapping("/all")
	@ResponseBody
	public MappingJacksonValue findItemCatAll(String callback){
		ItemCatResult itemCatResult = 
				itemCatService.findCacheItemCatAll();
		//创建对象
		MappingJacksonValue jacksonValue =
				new MappingJacksonValue(itemCatResult);
		//jsonP的请求格式要求 function method(data)
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
}
