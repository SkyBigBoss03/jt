package com.jt.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ItemDescMapper itemDescMapper;
	
	/**
	 * 1.获取记录总数  2.查询分页数据
	 * 
	 *  select * from tb_item limit 0,20
		select * from tb_item limit 20,20
		select * from tb_item limit 40,20
		
		第N页
		select * from tb_item limit (page-1) * rows,20
	 * 
	 */
	@Override
	public EasyUIResult findItemByPage(Integer page, Integer rows) {
		//查询记录总数
		int total = itemMapper.findItemConunt();
		
		int start = (page - 1) * rows;
		
		List<Item> itemList = itemMapper.findItemList(start,rows);
		
		return new EasyUIResult(total, itemList);
	}

	@Override
	public String findItemCatName(Long itemCatId) {
		
		return itemMapper.findItemCatById(itemCatId);
	}

	@Override
	public int findCount() {
		
		
		return itemMapper.TestfindCount();
	}
	
	/**
	 * 案例:
	 * 	 new Date   表示服务器时间                 时间可能存在差异
	 * 	 now()      表示数据库服务器时间
	 * 	 秒杀业务:
	 	 时间必须统一.要么都使用服务器时间 要么使用数据库时间
	 */
	@Override
	public void save(Item item,String desc) {
		//封装完整的数据
		item.setStatus(1); //数据正常
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		itemMapper.insert(item);
		//自己查询了数据为Item对象赋值
		
		//新增商品的描述信息
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Override
	public void updateItem(Item item,String desc) {
		//数据补齐
		item.setUpdated(new Date());
		//更新不为空的数据
		itemMapper.updateByPrimaryKeySelective(item);
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
	}

	@Override
	public void deleteItems(Long[] ids) {
		//实现关联删除
		itemDescMapper.deleteByIDS(ids);
		
		//利用通用Mapper实现自动删除
		itemMapper.deleteByIDS(ids);
	}
	
	/*
	 * 问题:如果使用该代码 则在一定程度影响效率.
	      解决:通过一个sql实现数据的动态修改
	*/
	@Override
	public void updateStatus(Long[] ids, int status) {
	/*	for (Long id : ids) {
			Item item = new Item();
			item.setStatus(status);
			item.setUpdated(new Date());
			item.setId(id);
			itemMapper.updateByPrimaryKeySelective(item);
		}*/
		itemMapper.updateStatus(ids,status);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		//利用通用Mapper实现数据查询
		return itemDescMapper.selectByPrimaryKey(itemId);
	}

	@Override
	public Item findItembyId(Long itemId) {
		
		return itemMapper.selectByPrimaryKey(itemId);
	}
	
	
	
	
	
	
	
	
	
}
