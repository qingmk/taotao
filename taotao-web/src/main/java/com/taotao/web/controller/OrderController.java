package com.taotao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.bean.Cart;
import com.taotao.web.bean.Item;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import com.taotao.web.threadLocal.UserThreadLocal;

@Controller
@RequestMapping("order")
public class OrderController {
	@Autowired
	private ItemService itemService;
	@Autowired
	private OrderService orderService;
	/*
	 * @Autowired private UserService userService;
	 */
	@Autowired
	private CartService cartService;

	/**
	 * 订单确认页面
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "{itemId}", method = RequestMethod.GET)
	public ModelAndView toOrder(@PathVariable("itemId") Long itemId) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("order");
		Item item = this.itemService.queryItemByItemId(itemId);
		mv.addObject("item", item);
		return mv;
	}
	/**
	 * 购物车下单
	 * 
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView toCartOrder() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("order-cart");
		User user = UserThreadLocal.get();
		List<Cart> carts = cartService.quertCartByUserId(user.getId());
		mv.addObject("carts", carts);
		return mv;
	}

	/**
	 * 订单提交页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "submit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitOrder(Order order/* , @CookieValue("TT_TOKEN") String token */) {
		Map<String, Object> result = new HashMap<>();

		// User user=this.userService.queryUserByToken(token);
		User user = UserThreadLocal.get();
		order.setUserId(user.getId());
		order.setBuyerNick(user.getUsername());
		String orderId = orderService.submitOrder(order);
		if (StringUtils.isEmpty(orderId)) {
			result.put("status", 300);
		} else {
			result.put("status", 200);
			result.put("data", orderId);
		}
		return result;
	}

	@RequestMapping(value = "success", method = RequestMethod.GET)
	public ModelAndView success(@RequestParam("id") String orderId) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("success");
		Order order = this.orderService.queryOrderByOrderId(orderId);
		mv.addObject("order", order);
		mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
		return mv;
	}

}
