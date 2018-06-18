package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Order;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private HttpClientService httpClient;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	
	@Override
	public String saveOrder(Order order) {
		String uri = "http://order.jt.com/order/create";
		try {
			String orderJSON = 
					objectMapper.writeValueAsString(order);
			Map<String,String> params = new HashMap<String,String>();
			
			params.put("orderJSON", orderJSON);
			String jsonData = httpClient.doPost(uri, params);
			SysResult sysResult = objectMapper.readValue(jsonData, SysResult.class);
			//判断返回值是否正确   200
			if(sysResult.getStatus() == 200){
				
				return (String) sysResult.getData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Order findOrderById(String id) {
		String uri = "http://order.jt.com/order/query/"+id;
		
		String orderJSON = httpClient.doGet(uri);
		Order order = null;
		try {
			order =  objectMapper.readValue(orderJSON, Order.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return order;
	}
	
	
	
	
	
	
	
	
}
