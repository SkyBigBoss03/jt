package com.jt.manage.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;

@Controller
@RequestMapping("/web/item")
public class WebItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/findItemById/{itemId}")
	@ResponseBody
	public Item findItemById(@PathVariable Long itemId){
		
		return itemService.findItembyId(itemId);
	}
	
	
	@RequestMapping("/findItemDescById/{itemId}")
	@ResponseBody
	public ItemDesc findItemDescById(@PathVariable Long itemId){
		
		return itemService.findItemDescById(itemId);
	}
	
	
	
	
	
	
	
	
	
}
