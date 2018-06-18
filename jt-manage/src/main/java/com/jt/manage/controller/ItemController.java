package com.jt.manage.controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	
	//同时入库3张表
	//http://localhost:8091/item/query?page=1&rows=20
	@RequestMapping("/query")
	@ResponseBody	//将对象转化为JSON串返回
	public EasyUIResult findItemByPage(Integer page,Integer rows){
		 
		return itemService.findItemByPage(page,rows);
	}
	
	
	/**
	 * 说明:为什么会出现乱码
	 * 原因:是因为@ResponseBody采取的策略不同
	 * 
	 * 如果返回的是对象 那么采用utf-8编码格式 
	 * 如果返回的是String串, 则采用iso-8859-1格式
	 * 
	 * 源码:StringHttpMessageConverter
	 * public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");
	 * 
	 * 解决办法:如果采用字符串返回,则使用手动的编码格式
	 * @RequestMapping(value="/cat/queryItemCatName",produces="text/html;charset=utf-8")
	 * 
	 * @param itemCatId
	 * @return
	 */
	//http://localhost:8091/item/cat/queryItemName
	@RequestMapping(value="/cat/queryItemCatName",produces="text/html;charset=utf-8")
	@ResponseBody
	public String findItemCatName(Long itemCatId){

		//根据商品分类ID查询商品分类的名称
		return itemService.findItemCatName(itemCatId);
	}
	
	@RequestMapping("/findCount")
	@ResponseBody
	public int findCount(){
		
		return itemService.findCount();
	}
	
	//item/save
	/**
	 * 添加desc参数,获取商品描述信息,
	 * 补充知识:SpringMVC中为对象进行赋值操作时,必须调用
	 * setXXXX()方法,否则参数不会赋值.
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveItem(Item item,String desc){
		try {
			itemService.save(item,desc);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"商品新增失败");
	}
	
	
	//商品的修改
	@RequestMapping("/update")
	@ResponseBody
	public SysResult updateItem(Item item,String desc){
		try {
			itemService.updateItem(item,desc);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return SysResult.build(201,"修改失败");
	}
	
	/**
	 * {"ids":ids}   同名提交: 1111,2222,33333,444444
	 * 说明:通过浏览器传递的数据都是String 字符串
	 * 
	 * 如果采用了SpringMVC,则可以实现数据的自动的封装
	 * 1.可以为对象中的属性赋值,
	 * 2.如果参数以","号进行拼接,则可以直接转化为[]格式
	 * 3.还以为List集合赋值/Map赋值
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public SysResult deleteItems(Long[] ids){
		try {
			itemService.deleteItems(ids);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"删除失败");
	}
	
	//实现商品的下架
	@RequestMapping("/instock")
	@ResponseBody
	public SysResult  instock(Long[] ids){
		try {
			int status = 2;	//商品下架
			itemService.updateStatus(ids,status);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"下架失败");
	}
	
	@RequestMapping("/reshelf")
	@ResponseBody
	public SysResult reshelf(Long[] ids){
		try {
			int status = 1;	//上架
			itemService.updateStatus(ids,status);
			
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.build(201,"上架失败");
	}
	
	///item/query/item/desc/商品的Id
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDescById(
			@PathVariable Long itemId){
		try {
			ItemDesc itemDesc = 
					itemService.findItemDescById(itemId);
			return SysResult.oK(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"商品描述信息查询失败");
	}
	
	
	
}
