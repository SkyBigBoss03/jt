package com.jt.manage.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.EasyUITreeResult;
import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	//注入jedis对象
	//@Autowired
	//private Jedis jedis;
	
	//@Autowired
	//private RedisService redisService;
	
	//集群的方式操作redis
	@Autowired
	private JedisCluster jedisCluster;
	
	
	
	//实现商品分类查询
	@Override
	public List<ItemCat> findItemCatList(Long parentId){
		ItemCat itemCat = new ItemCat();
		itemCat.setParentId(parentId);
		return itemCatMapper.select(itemCat);
	}

	/**
	 * 通用Mapper中的使用方法:
	 * 如果使用对象查询,则根据属性中的非空属性充当where条件
	 */
	@Override
	public List<EasyUITreeResult> findCatListById(Long parentId) {
		List<EasyUITreeResult> treeList = new ArrayList<EasyUITreeResult>();
		//获取商品分类集合信息 调动业务方法
		List<ItemCat> itemCatList = findItemCatList(parentId);	
		for (ItemCat itemCat : itemCatList) {
			EasyUITreeResult result = new EasyUITreeResult();
			result.setId(itemCat.getId());
			result.setText(itemCat.getName());
			//如果是父级则关闭子节点
			String state = itemCat.getIsParent() ? "closed" : "open";
			result.setState(state);
			treeList.add(result);
		}
		return treeList;
	}

	/**
	 * 1.当查询分类信息时首先应该查询redis缓存   key
	   2.如果查询的数据为null,则查询数据库
	   3.将返回的数据转化为JSON串,之后保存到redis中
	   4.如果查询数据不为null.则获取redis中的JSON数据,转化为对象之后返回.
	   @ResponseBoby
	 */
	@Override
	public List<EasyUITreeResult> findCacheItemCatList(Long parentId) {
		List<EasyUITreeResult> treeList = new ArrayList<EasyUITreeResult>();
		String key = "ITEM_CAT_"+parentId;
		String jsonData = jedisCluster.get(key);
		try {	
			if(StringUtils.isEmpty(jsonData)){
				//获取的缓存数据为空
				treeList = findCatListById(parentId);
				//将对象转化为JSON串的格式
				String json = objectMapper.writeValueAsString(treeList);
				jedisCluster.set(key, json);
			}else{
				//缓存中有数据 将json串转化为对象
				//EasyUITreeResult[] treeArrays = 
				//objectMapper.readValue(jsonData, EasyUITreeResult[].class);
				//treeList = Arrays.asList(treeArrays);
				treeList = objectMapper.readValue(jsonData, treeList.getClass());
			}
		} catch (Exception e) {
				e.printStackTrace();
			}
		return treeList;
	}
	
	/**
	 * 问题1:数据如何查询 1,2,3
	 * 说明:如果使用根据一级查二级 根据二级查三级的策略 
	 * 		数据库的负载过高.因为查询数据库的次数太多了.
	 * 改进:通过一次查询 将数据分类结构准备好
	 */
	@Override
	public ItemCatResult findItemCatAll() {
		
		//查询所有的商品分类信息 1次数据库
		List<ItemCat> itemCatList = itemCatMapper.select(null);
		
		//封装商品的分类的数据类型
		Map<Long,List<ItemCat>> map = new HashMap<Long,List<ItemCat>>();
		for (ItemCat itemCat : itemCatList) {
			if(map.containsKey(itemCat.getParentId())){
				//表示map中已经有父级Id,获取父级集合追加元素
				map.get(itemCat.getParentId()).add(itemCat);
			}else{
				List<ItemCat> tempList = new ArrayList<ItemCat>();
				tempList.add(itemCat);	//添加第一个元素
				map.put(itemCat.getParentId(),tempList);
			}
		}
		
		//返回的数据的类型
		ItemCatResult itemCatResult = new ItemCatResult();
		
		//创建一级标题集合信息
		List<ItemCatData> itemCatDataList1 = new ArrayList<ItemCatData>();
		
		//获取一级商品分类菜单  父级 0
		for (ItemCat itemCat1 : map.get(0L)) {
			ItemCatData itemCatData1 = new ItemCatData();
			itemCatData1.setUrl("/products/"+itemCat1.getId()+".html");
			itemCatData1.setName("<a href='"+itemCatData1.getUrl()+"'>"+itemCat1.getName()+"</a>");
			
			//准备2级商品list集合
			List<ItemCatData> itemCatDataList2 = new ArrayList<ItemCatData>();
			
			//封装二级商品分类信息
			for (ItemCat itemCat2 : map.get(itemCat1.getId())) {
				ItemCatData itemCatData2 = new ItemCatData();
				itemCatData2.setUrl("/products/"+itemCat2.getId());
				itemCatData2.setName(itemCat2.getName());
				
				//封装三级商品分类菜单
				List<String> itemCatDataList3 = new ArrayList<String>();
				
				//循环遍历3级商品分类菜单
				for (ItemCat itemCat3 : map.get(itemCat2.getId())) {
					
					itemCatDataList3.add("/products/"+itemCat3.getId()+"|"+itemCat3.getName());
				}
				itemCatData2.setItems(itemCatDataList3);
				itemCatDataList2.add(itemCatData2);	
			}
			itemCatData1.setItems(itemCatDataList2);
			itemCatDataList1.add(itemCatData1);
			//为了页面显示正常则只显示14条
			if(itemCatDataList1.size()>13){
				break;
			}
		}
		
		itemCatResult.setItemCats(itemCatDataList1);
		return itemCatResult;
	}
	
	/**
	 * 1.让用户的请求先查询缓存
	 * 2.如果缓存中没有数据则查询数据库
	 * 	 2.1 将数据写入到缓存中 json串
	 * 3.如果缓存中有数据则直接返回对象
	 * 	 3.2需要将json数据转化为java对象
	 */
	@Override
	public ItemCatResult findCacheItemCatAll() {
		String key = "ITEM_CAT_ALL";
		String jsonData = jedisCluster.get(key);
		try {
			if(StringUtils.isEmpty(jsonData)){
				//为null 则查询后台数据库
				ItemCatResult itemCatResult = findItemCatAll();
				//将数据写入缓存中
				String json = 
						objectMapper.writeValueAsString(itemCatResult);
				jedisCluster.set(key, json);	
				return itemCatResult;
			}else{
				//表示缓存数据中有数据
				ItemCatResult itemCatResult = 
				objectMapper.readValue(jsonData,ItemCatResult.class);
				System.out.println("调用缓存成功!!!!");
				return itemCatResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
}
