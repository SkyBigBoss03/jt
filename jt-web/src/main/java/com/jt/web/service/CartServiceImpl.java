package com.jt.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private HttpClientService httpClient;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	
	
	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		String url = "http://cart.jt.com/cart/query/"+userId;
		String jsonData = httpClient.doGet(url);
		List<Cart> cartList = null;
		//先将json串转化为SysResult对象
		try {
			SysResult sysResult = objectMapper.readValue(jsonData, SysResult.class);
			cartList = (List<Cart>) sysResult.getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cartList;
	}



	@Override
	public void updateCartNum(Long userId, Long itemId, Integer num) {
		
		//1.定义远程uri
		String uri = "http://cart.jt.com/cart/update/num/"+userId+"/"+itemId+"/"+num;
		//2.发起请求
		httpClient.doGet(uri);
		System.out.println("前台调用完成");
	}



	@Override
	public void saveCart(Cart cart) {
		String uri = "http://cart.jt.com/cart/save";
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("userId",cart.getUserId()+"");
		params.put("itemId", cart.getItemId()+"");
		params.put("itemTitle", cart.getItemTitle());
		params.put("itemImage", cart.getItemImage());
		params.put("itemPrice", cart.getItemPrice()+"");
		params.put("num", cart.getNum()+"");
		httpClient.doPost(uri, params);
		
	}



	@Override
	public void deleteCart(Long itemId, Long userId) {
		
		String uri = "http://cart.jt.com/cart/delete/"+userId+"/"+itemId;
		
		httpClient.doGet(uri);
		
	}
	
	
	
	
	
	
	
	
	

}
