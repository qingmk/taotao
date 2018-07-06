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
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.impl.ItemService;

@Controller
@RequestMapping("item")
public class ItemController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
	@Autowired
	private ItemService itemService;
	
	/*
	 * @Autowired private ItemDescService itemDescService;
	 */

	/**
	 * 保存商品
	 * 
	 * @param item
	 * @return
	 */

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,
			@RequestParam("itemParams") String itemParams) {
		try {

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("新增商品,item={},desc={}", item, desc);
			}
			this.itemService.saveItem(item, desc, itemParams);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("新增商品成功,itemid={}", item.getId());
			}
			// 成功创建
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			LOGGER.error("新增商品失败,商品名字" + item.getTitle() + ",商品cid" + item.getCid(), e);

		}
		// 出现错误
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 查询商品
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<EasyUIResult> queryItem(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "30") Integer rows) {
		
		
		
		
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询商品分页");
			}
			PageInfo<Item> pageinfo = itemService.querPageyList(page, rows);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询商品分页成功");
			}
			EasyUIResult easyUIResult = new EasyUIResult(pageinfo.getTotal(), pageinfo.getList());

			return ResponseEntity.ok().body(easyUIResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("查询商品失败", e);

		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 更新商品
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> UpdateItem(Item item, @RequestParam("desc") String desc,
			@RequestParam("itemParams") String itemParams) {
		try {
			/*
			 * item.setStatus(1); item.setId(null); this.itemService.saveSelect(item);
			 * ItemDesc itemDesc= new ItemDesc(); itemDesc.setItemId(item.getId());
			 * itemDesc.setItemDesc(desc); this.itemDescService.saveSelect(itemDesc);
			 */
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("修改商品,item={},desc={}", item, desc);
			}
			this.itemService.updateItem(item, desc, itemParams);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("修改商品成功,itemid={}", item.getId());
			}
			// 成功创建
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			LOGGER.error("修改商品失败,商品名字" + item.getTitle() + ",商品cid" + item.getCid(), e);
			// 出现错误
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	/**
	 * 根据商品id查询商品
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "{itemId}", method = RequestMethod.GET)
	public ResponseEntity<Item> queryByItemId(@PathVariable(value = "itemId") Long itemId) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询商品,itemid="+itemId);
			}
			Item item = this.itemService.queryByid(itemId);
			if(null==item) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询商品成功itemid="+itemId);
			}

			return ResponseEntity.ok().body(item);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("查询商品失败itemid="+itemId, e);

		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

}
