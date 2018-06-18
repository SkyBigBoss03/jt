package com.jt.manage.service;

import com.jt.common.vo.EasyUIResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

public interface ItemService {

	EasyUIResult findItemByPage(Integer page, Integer rows);

	String findItemCatName(Long itemCatId);

	int findCount();

	void save(Item item, String desc);

	void updateItem(Item item, String desc);

	void deleteItems(Long[] ids);

	void updateStatus(Long[] ids, int status);

	ItemDesc findItemDescById(Long itemId);
	
	//根据商品的Id号查询数据
	Item findItembyId(Long itemId);

}
