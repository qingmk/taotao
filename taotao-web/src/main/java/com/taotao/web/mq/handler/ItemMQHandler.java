package com.taotao.web.mq.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;

public class ItemMQHandler {
	@Value("${REDIS_WEB_ITEM_KEY_DETAIL}")
	private String REDIS_KEY;
	@Autowired
	private RedisService redisService;
	private final ObjectMapper MAPPER = new ObjectMapper();
	public void execute(String msg) {
		try {
			JsonNode node=MAPPER.readTree(msg);
			Long itemId=node.get("itemId").asLong();
			redisService.del(REDIS_KEY+itemId);
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
