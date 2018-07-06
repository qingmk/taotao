package com.taotao.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ItemParamMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemParam;

@Service
public class ItemParamService extends BaseService<ItemParam> {
	@Autowired
	private ItemParamMapper itemParamMapper;

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageInfo<ItemParam> querPageyList(Integer page, Integer rows) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, rows);
		Example example = new Example(Item.class);
		example.setOrderByClause("updated Desc");
		List<ItemParam> list = this.itemParamMapper.selectByExample(example);

		return new PageInfo<ItemParam>(list);
	}
	
	
	
	

}
