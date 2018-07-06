package com.taotao.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content> {

	@Autowired
	private ContentMapper contentMapper;
	public PageInfo<Content> queryList(Integer page, Integer rows, long categoryId) {
	
		PageHelper.startPage(page, rows);
		List<Content> list=contentMapper.selectByCategoryId(categoryId);
		PageInfo<Content> pageInfo = new PageInfo<>(list);
		
		return pageInfo;
	}

	

}
