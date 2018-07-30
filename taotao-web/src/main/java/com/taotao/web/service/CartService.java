package com.taotao.web.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Cart;

@Service
public class CartService {
	@Autowired
	private ApiService apiService;
	@Value("${TAOTAO_CART_URL}")
	private String TAOTAO_CART_URL;
	private final ObjectMapper MAPPER = new ObjectMapper();
	public List<Cart> quertCartByUserId(Long userId) {
		String url=TAOTAO_CART_URL+"/service/cart?userId="+userId;
		
		try {
			String jsonData=apiService.doGet(url);
			if(StringUtils.isEmpty(jsonData)) {
				return null;
			}
			return MAPPER.readValue(jsonData, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
