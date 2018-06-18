package com.jt.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.cart.pojo.Cart;
import com.jt.common.service.BaseService;

@Service
public class CartServiceImpl extends BaseService<Cart>implements CartService {
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListById(Long userId) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		List<Cart> cartList = cartMapper.select(cart);
		return cartList;
	}

	
	@Override
	public void updateCartNum(Long userId, Long itemId, Integer num) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cart.setNum(num);
		cart.setUpdated(new Date());
		cartMapper.updateCartNum(cart);
	}


	/**
	 * 1.先根据itemId和userId查询数据库信息
	 * 2.如果查询的结果为null则新增购物车
	 * 3.如果查询的结果不为null,则做数量的更新操作
	 */
	@Override
	public void saveCart(Cart cart) {
		
		Cart cartDB = new Cart();
		cartDB.setItemId(cart.getItemId());
		cartDB.setUserId(cart.getUserId());
		
		//查询购物车信息
		//Cart cartTemp = cartMapper.select(cartDB).get(0);
		
		Cart cartTemp = super.queryByWhere(cartDB);

		if(cartTemp == null){
			//准备购物车数据
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else{
			//数据库中有该信息 更新商品数量
			int num = cartTemp.getNum() + cart.getNum();
			cartTemp.setNum(num);
			cartTemp.setUpdated(new Date());
			cartMapper.updateByPrimaryKeySelective(cartTemp);
		}
	}


	@Override
	public void deleteCart(Cart cart) {
		
		cartMapper.delete(cart);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
