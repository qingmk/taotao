package com.taotao.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.cart.bean.Item;
import com.taotao.cart.bean.User;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.threadLocal.UserThreadLocal;
/**
 * 登录状态下购物车操作
 * @author Administrator
 *
 */
@Service
public class CartService {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartMapper cartMapper;
	/**
	 * 把商品加入购物车
	 * @param itemId
	 */
	public void addItemToCart(Long itemId) {
		User user=UserThreadLocal.get();
		Cart record=new Cart();
		record.setItemId(itemId);
		record.setUserId(user.getId());
		Cart result=this.cartMapper.selectOne(record);
		if(null==result) {
			//商品不存在,查询商品并加入cart
			Item item=itemService.queryItemByItemId(itemId);
			if(null==item){
				return ;
			}
			Cart cart =new Cart();
			cart.setItemId(itemId);
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cart.setUserId(user.getId());
			cart.setNum(1);
			cart.setItemImage(item.getImages()[0]);
			cart.setItemTitle(item.getTitle());
			cart.setItemPrice(item.getPrice());
			cartMapper.insert(cart);
		}else {
			//商品已经存在,数量相加并更新
			result.setNum(result.getNum()+1);
			result.setUpdated(new Date());
			cartMapper.updateByPrimaryKeySelective(result);
		}
	}
	/**
	 * 查询购物车列表
	 * @return
	 */
	public List<Cart> queryCartList() {
		User user=UserThreadLocal.get();
		return queryCartList(user.getId());
		
	}
	/**
	 * 更新商品信息
	 * @param itemId
	 * @param num 最总商品购买数目
	 */
	public void updateNum(Long itemId, Integer num) {
	
		
		User user=UserThreadLocal.get();
		Example example = new Example(Cart.class);
		example.createCriteria().andEqualTo("itemId", itemId).andEqualTo("userId", user.getId());
		Cart cart = new Cart();
		cart.setNum(num);
		cart.setUpdated(new Date());
		cartMapper.updateByExampleSelective(cart,example);
	}
	/**
	 * 删除购物车内商品
	 * @param itemId
	 */
	public void deleteCart(Long itemId) {
		
		User user=UserThreadLocal.get();
		Example example = new Example(Cart.class);
		example.createCriteria().andEqualTo("itemId", itemId).andEqualTo("userId", user.getId());
		cartMapper.deleteByExample(example);
	
	}
	public List<Cart> queryCartList(Long userId) {
		Example example = new Example(Cart.class);
		example.createCriteria().andEqualTo("userId", userId);
		example.setOrderByClause("created");
		return cartMapper.selectByExample(example);
	}
	
	

}
