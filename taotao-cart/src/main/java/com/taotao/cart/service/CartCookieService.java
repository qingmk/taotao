package com.taotao.cart.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Item;
import com.taotao.cart.pojo.Cart;
import com.taotao.common.utils.CookieUtils;

@Service
/**
 * 未登录下操纵购物车
 * 
 * @author Administrator
 *
 */
public class CartCookieService {
	@Value("${COOKIENAME}")
	private String COOKIENAME;
	@Value("${COOKIETIME}")
	private int COOKIETIME;
	private final ObjectMapper MAPPER = new ObjectMapper();
	@Autowired
	private ItemService itemService;

	public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
		List<Cart> carts = queryCartList(request);
		// 遍历集合，判断cart是否存在
		Cart cart = null;
		for (Cart c : carts) {
			if (c.getItemId().longValue() == itemId) {
				cart = c;
				break;
			}
		}
		if (null == cart) {
			// 商品不存在，添加
			Item item = itemService.queryItemByItemId(itemId);
			if (null == item) {
				return;
			}
			Cart result = new Cart();
			result.setItemId(itemId);
			result.setCreated(new Date());
			result.setUpdated(result.getCreated());
			result.setNum(1);
			result.setItemImage(item.getImages()[0]);
			result.setItemTitle(item.getTitle());
			result.setItemPrice(item.getPrice());
			carts.add(result);

		} else {
			// 商品存在，数目增加
			cart.setNum(cart.getNum() + 1);
			cart.setUpdated(new Date());
		}
		// 写入cookie
		try {
			CookieUtils.setCookie(request, response, COOKIENAME, MAPPER.writeValueAsString(carts), COOKIETIME, true);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}

	}

	public List<Cart> showCartList(HttpServletRequest request) {
		// TODO Auto-generated method stub
		List<Cart> carts = queryCartList(request);
		return carts;
	}

	/**
	 * 查询cookie并序列化为cart集合对象
	 * @param request
	 * @return
	 */
	private List<Cart> queryCartList(HttpServletRequest request) {
		String cookieValue = CookieUtils.getCookieValue(request, COOKIENAME, true);
		List<Cart> carts = null;
		if (StringUtils.isEmpty(cookieValue)) {
			carts = new ArrayList<>();
		} else {
			try {
				carts = MAPPER.readValue(cookieValue,
						MAPPER.getTypeFactory().constructCollectionLikeType(java.util.List.class, Cart.class));
			} catch (IOException e) {
				e.printStackTrace();
				carts = new ArrayList<>();
			}
		}
		return carts;
	}

	public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {

		List<Cart> carts = queryCartList(request);
		// 遍历集合，判断cart是否存在
		Cart cart = null;
		for (Cart c : carts) {
			if (c.getItemId().longValue() == itemId) {
				cart = c;
				break;
			}
		}

		// 商品存在，数目增加
		cart.setNum(cart.getNum() + 1);
		cart.setUpdated(new Date());

		// 写入cookie
		try {
			CookieUtils.setCookie(request, response, COOKIENAME, MAPPER.writeValueAsString(carts), COOKIETIME, true);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
	}

}
