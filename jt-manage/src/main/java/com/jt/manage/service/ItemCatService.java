package com.jt.manage.service;

import java.util.List;

import com.jt.common.vo.EasyUITreeResult;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.pojo.ItemCat;

public interface ItemCatService {
	//获取EasyTree树形结构集合
	List<EasyUITreeResult> findCatListById(Long parentId);
	
	//获取ItemCatList集合
	List<ItemCat> findItemCatList(Long parentId);

	List<EasyUITreeResult> findCacheItemCatList(Long parentId);

	ItemCatResult findItemCatAll();
	
	//实现缓存的处理
	ItemCatResult findCacheItemCatAll();
}
