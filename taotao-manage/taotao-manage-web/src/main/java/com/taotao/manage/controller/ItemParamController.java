package com.taotao.manage.controller;

import java.util.Date;

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
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.impl.ItemParamService;

@Controller
@RequestMapping("item/param")
public class ItemParamController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemParamController.class);
	@Autowired
	private ItemParamService itemParamService;

	/**
	 * 查询商品参数分页
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ResponseEntity<EasyUIResult> queryItem(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "rows", defaultValue = "30") Integer rows) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询商品分页");
			}
			PageInfo<ItemParam> pageinfo = itemParamService.querPageyList(page, rows);
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
	 * 根据商品类目id来获取商品规格参数
	 * 
	 * @param itemCatId
	 * @return
	 */
	@RequestMapping(value = "{itemCatId}", method = RequestMethod.GET)
	public ResponseEntity<ItemParam> queryItemParam(@PathVariable(value = "itemCatId") long itemCatId) {

		try {
			ItemParam record = new ItemParam();
			record.setItemCatId(itemCatId);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始查询商品规格参数,itemcatid=" + itemCatId);
			}
			ItemParam result = this.itemParamService.queryOne(record);
			if (null == result) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("查询商品规格参数不存在,itemcatid=" + itemCatId);
				}

				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("查询商品规格参数成功,resluit=" + result.getParamData());
				}
				return ResponseEntity.ok().body(result);
			}
		} catch (Exception e) {

			LOGGER.error("查询商品规格参数出现错误，itemcatid=" + itemCatId, e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 根据商品类目id来保存商品规格参数
	 * 
	 * @param itemCatId
	 * @param paramData
	 * @return
	 */
	@RequestMapping(value = "{itemCatId}", method = RequestMethod.POST)
	public ResponseEntity<Void> saveItemParam(@PathVariable(value = "itemCatId") long itemCatId,
			@RequestParam(value = "paramData") String paramData) {

		try {
			ItemParam itemParam = new ItemParam();
			itemParam.setItemCatId(itemCatId);
			itemParam.setParamData(paramData);
			itemParam.setCreated(new Date());
			itemParam.setUpdated(itemParam.getCreated());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始保存数据,itemcatid=" + itemCatId);
			}
			this.itemParamService.save(itemParam);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("保存数据成功,paramData=" + paramData);
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("保存数据出错，itemcatid=" + itemCatId, e);
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}

	/**
	 * 根据id删除参数数据
	 */
/*
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteItemParam(@RequestParam(value = "ids") long[] ids) {

		List<Object> list2 = new ArrayList<Object>();

		for (long s : ids) {
			list2.add(s);
			System.out.println(s);
		}

		try {

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始删除数据,第一个itemcatid=" + list2.get(0));
			}

			this.itemParamService.deleteByids(ItemParam.class, "itemCatId", list2);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("成功删除数据,最后一个itemcatid=" + list2.get(list2.size() - 1));
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
		
			LOGGER.error("删除数据出错，第一个itemcatid=" + list2.get(0), e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}*/

}
