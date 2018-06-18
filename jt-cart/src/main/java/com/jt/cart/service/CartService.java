package com.jt.cart.service;

import java.util.List;

import com.jt.cart.pojo.Cart;

public interface CartService {

	List<Cart> findCartListById(Long userId);

	void updateCartNum(Long userId, Long itemId, Integer num);

	void saveCart(Cart cart);

	void deleteCart(Cart cart);
	
}
