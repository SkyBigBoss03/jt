package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUITreeResult;
import com.jt.manage.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	
	/**
	 * 说明:基于easyUI的树形结构展现,需要定义3个属性类型
	 * id:标识选项id
	 * text: 节点的名称
	 * state: open 或者 closed
	 * url:http://localhost:8091/item/cat/list
	 * 注解说明:
	 * 	@RequestParam
	 * 		value:接收参数的名称
	 * 		defaultValue:默认值
	 * 		required:true  该参数必须传递
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeResult> findCatListById(
			@RequestParam(value="id",defaultValue="0")Long parentId){
		//展现商品分类的一级标题
		List<EasyUITreeResult> treeList =
				itemCatService.findCacheItemCatList(parentId);
		return treeList;
	}
}
