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

import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.manage.service.impl.ItemParamItemService;

@Controller
@RequestMapping("item/param/item")
public class ItemParamItemController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemParamItemController.class);
	@Autowired
	private ItemParamItemService itemParamItemService;
	
	/**
	 * 根据商品id来获取商品规格参数数据
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "{itemId}", method = RequestMethod.GET)
	public ResponseEntity<ItemParamItem> queryItemParam(@PathVariable(value = "itemId") long itemId) {

		try {
			ItemParamItem record = new ItemParamItem();
			record.setItemId(itemId);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始查询商品规格参数数据,itemid=" + itemId);
			}
			ItemParamItem result = this.itemParamItemService.queryOne(record);
			if (null == result) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("商品规格参数数据不存在,itemid=" + itemId);
				}

				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("查询商品规格参数成功,resluit=" + result.getParamData());
				}
				return ResponseEntity.ok().body(result);
			}
		} catch (Exception e) {

			LOGGER.error("查询商品规格参数出现错误，itemcatid=" + itemId, e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
}
