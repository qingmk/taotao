package com.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.impl.ItemDescService;

@Controller
@RequestMapping("item/desc")
public class ItemDescController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemDescController.class);
	@Autowired
	private ItemDescService itemDescService;
	/*
	 * @Autowired private ItemDescService itemDescService;
	 */

	/**
	 * 查询商品描述
	 */
	@RequestMapping(method = RequestMethod.GET,value="{itemId}")
	public ResponseEntity<ItemDesc> queryItemDesc(@PathVariable("itemId") Long itemId) {
	
		try {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始查询商品描述，itemId:"+itemId);
			}
			ItemDesc itemDesc=itemDescService.queryByid(itemId);
			if(null==itemDesc) {
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("商品描述不存在，itemId:"+itemId);
				}
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询商品描述成功，itemId:"+itemId+",itemDesc:"+itemDesc.getItemDesc());
			}
			return ResponseEntity.ok().body(itemDesc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("查询商品描述失败，itemId:"+itemId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
	
	}

}
