package com.taotao.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.ItemCatMapper;
import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;
@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private ItemCatMapper catMapper;

	/**
	 * 根据父节点ID查询商品类目
	 */
	@Override
	public List<ItemCat> queryItemCatListByParentId(Long parentId) {
		ItemCat itemCat = new ItemCat();
		itemCat.setParentId(parentId);
		List<ItemCat> list = catMapper.select(itemCat);
		return list;
	}

}
