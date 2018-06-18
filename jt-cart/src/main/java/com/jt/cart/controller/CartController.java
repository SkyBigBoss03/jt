package com.jt.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jt.cart.pojo.Cart;
import com.jt.cart.service.CartService;
import com.jt.common.vo.SysResult;

//@Controller
@RestController  //@Controller + @ResponseBody
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	//根据用户Id查询购物车信息
	@RequestMapping("/query/{userId}")
	@ResponseBody
	public SysResult findCartListById(@PathVariable Long userId){
		try {
			List<Cart> cartList = cartService.findCartListById(userId);
			return SysResult.oK(cartList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"查询购物车失败");
	}
	
	
	@RequestMapping("/update/num/{userId}/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(
			@PathVariable Long userId,
			@PathVariable Long itemId,
			@PathVariable Integer num){
		try {
			cartService.updateCartNum(userId,itemId,num);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "调用失败");
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveCart(Cart cart){
		try {
			cartService.saveCart(cart);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"新增购物车信息");
	}
	
	
	@RequestMapping("/delete/{userId}/{itemId}")
	public SysResult deleteCart(@PathVariable Long userId, @PathVariable Long itemId){
		try {
			
			Cart cart = new Cart();
			cart.setUserId(userId);
			cart.setItemId(itemId);
			cartService.deleteCart(cart);
			return SysResult.oK();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.build(201,"删除失败");
	}
	
	
	
	
	
	
}
