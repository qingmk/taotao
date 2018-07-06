package com.taotao.manage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.impl.ItemCatService;

@Controller
@RequestMapping("item/cat")
public class ItemCatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemCatController.class);
	/*@Autowired
	private ItemCatService itemcatService;*/
	@Autowired
	private ItemCatService itemcatService;

	/**
	 * 通过商品父节点ID查询商品类目
	 * @param parentId
	 * @return
	 */
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ItemCat>> queryItemCatListByParentId(@RequestParam(value="id",defaultValue="0")Long parentId) {
		try {
			//List<ItemCat> list = itemcatService.queryItemCatListByParentId(parentId);
			
			
			
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始查询商品类目,parentid:"+parentId);
			}
			
			ItemCat itemCat = new ItemCat();
			itemCat.setParentId(parentId);
			List<ItemCat> list = itemcatService.queryListBYWhere(itemCat);

			if(null==list||list.isEmpty()) {
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("查询商品类目不存在,parentid:"+parentId);
				}
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}else {
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("查询商品类目成功,parentid:"+parentId);
				}
				
				return ResponseEntity.ok().body(list);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("查询商品类目失败,parentId:"+parentId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		

	}

}
