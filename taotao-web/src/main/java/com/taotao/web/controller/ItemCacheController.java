package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.service.RedisService;

@RequestMapping("item/cache/")
@Controller
public class ItemCacheController {
	@Autowired
	private RedisService redisService;
	@Value("${REDIS_WEB_ITEM_KEY_DETAIL}")
	private String REDIS_KEY;
	@RequestMapping(value = "{itemId}", method = RequestMethod.GET)

	public ResponseEntity<Void> showDetails(@PathVariable(value = "itemId") Long itemId) {
		try {
			this.redisService.del(REDIS_KEY+itemId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}
