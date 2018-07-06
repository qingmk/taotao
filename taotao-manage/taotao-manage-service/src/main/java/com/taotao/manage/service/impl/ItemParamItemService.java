package com.taotao.manage.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.pojo.ItemParamItem;
@Service
public class ItemParamItemService extends BaseService<ItemParamItem> {
	@Autowired
	private  ItemParamItemMapper itemParamItemMapper;
	/**
	 * 更新商品参数数据
	 * @param itemId
	 * @param itemParams
	 */
	public void updateItemParamItem(Long itemId, String itemParams) {
		
		/**
		 * 设置更新条件
		 */
		Example example= new Example(ItemParamItem.class);
		example.createCriteria().andEqualTo("itemId", itemId);
		/**
		 * 设置更新数据
		 */
		ItemParamItem itemParamItem=new ItemParamItem();
		itemParamItem.setParamData(itemParams);
		itemParamItem.setItemId(itemId);
		itemParamItem.setCreated(null);
		itemParamItem.setUpdated(new Date());
		this.itemParamItemMapper.updateByExampleSelective(itemParamItem, example);
	}
	
	
}
