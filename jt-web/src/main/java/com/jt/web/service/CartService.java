package com.jt.web.service;

import java.util.List;

import com.jt.web.pojo.Cart;

public interface CartService {

	List<Cart> findCartListByUserId(Long userId);

	void updateCartNum(Long userId, Long itemId, Integer num);

	void saveCart(Cart cart);

	void deleteCart(Long itemId, Long userId);

}
