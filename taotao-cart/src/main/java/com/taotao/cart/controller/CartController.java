package com.taotao.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.cart.bean.User;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartCookieService;
import com.taotao.cart.service.CartService;
import com.taotao.cart.threadLocal.UserThreadLocal;

@Controller
@RequestMapping("cart")
public class CartController {
	@Autowired
	private CartService cartService;
	@Autowired
	private CartCookieService cartCookieService;

	@RequestMapping(value = "{itemId}", method = RequestMethod.GET)
	/**
	 * 商品加入购物车并重定向到购物车
	 * 
	 * @param itemId
	 * @return
	 */
	public String addItemToCart(@PathVariable("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response) {
		User user = UserThreadLocal.get();
		if (null == user) {
			// 未登录状态逻辑
			this.cartCookieService.addItemToCart(itemId,request,response);
		} else {
			// 登录状态逻辑
			this.cartService.addItemToCart(itemId);
		}
		// 重定向
		return "redirect:/cart/list.html";
	}

	/**
	 * 显示购物车列表视图
	 * 
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView showCartList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		User user = UserThreadLocal.get();
		List<Cart> cartList = null;
		if (null == user) {
			// 未登录状态逻辑
			cartList =this.cartCookieService.showCartList(request);
		} else {
			// 登录状态逻辑
			cartList = cartService.queryCartList();
		}

		mv.setViewName("cart");
		mv.addObject("cartList", cartList);
		return mv;
	}
	/**
	 * 通过用户id获取cartList
	 * @param UserId
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,params="userId")
	public ResponseEntity<List<Cart>> queryCartList(@RequestParam("userId")Long UserId){
		try {
			List<Cart> cartList = cartService.queryCartList(UserId);
			if(null==cartList||cartList.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.status(HttpStatus.OK).body(cartList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 更新商品数量
	 * 
	 * @param num
	 *            购买商品数量
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "update/{itemId}/{num}", method = RequestMethod.POST)
	public ResponseEntity<Void> updateNum(@PathVariable("num") Integer num, @PathVariable("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response) {
		User user = UserThreadLocal.get();
		if (null == user) {
			// 未登录状态逻辑
			this.cartCookieService.updateNum(itemId, num,request,response);
		} else {
			// 登录状态逻辑
			this.cartService.updateNum(itemId, num);
		}

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 删除购物车内商品
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
	public String deleteCart(@PathVariable("itemId") Long itemId) {
		User user = UserThreadLocal.get();
		if (null == user) {
			// 未登录状态逻辑

		} else {
			// 登录状态逻辑
			this.cartService.deleteCart(itemId);
		}

		// 重定向
		return "redirect:/cart/list.html";
	}
}
