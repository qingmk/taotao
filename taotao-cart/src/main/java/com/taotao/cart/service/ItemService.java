package com.taotao.cart.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Item;
import com.taotao.common.service.ApiService;

@Service
public class ItemService {
	private final ObjectMapper MAPPER = new ObjectMapper();
	@Autowired
	private ApiService apiService;
	@Value("${TAOTAO_MANAGE_URL}")
	private String MANAGE_URL;
	public Item queryItemByItemId(Long itemId) {
		String url=MANAGE_URL+"/rest/item/"+itemId;
		try {
			String jsonData=apiService.doGet(url);
			if(StringUtils.isEmpty(jsonData)) {
				return null;
			}
			return MAPPER.readValue(jsonData, Item.class);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}

}
