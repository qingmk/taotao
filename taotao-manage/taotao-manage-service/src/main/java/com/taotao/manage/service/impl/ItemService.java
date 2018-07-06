package com.taotao.manage.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
@Service
public class ItemService extends BaseService<Item> {
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescService itemDescService;
	@Autowired
	private ItemParamItemService itemParamItemService;
	@Value("${WEB_BASE_URL}")
	private String WEB_BASE_URL;
	@Autowired
	private ApiService apiService;
	@Autowired
	private RedisService redisService;
	/**
	 * 保存商品数据
	 * @param item 商品数据
	 * @param desc 商品描述
	 * @param itemParams 商品参数
	 */
	public void saveItem(Item item, String desc,String itemParams) {
		
		item.setStatus(1);
		item.setId(null); 
		super.save(item);
		ItemDesc itemDesc= new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		this.itemDescService.save(itemDesc);
		
		if(StringUtils.indexOf(itemParams, "v")==-1) {
			
			return;
		}
			
		
		ItemParamItem itemParamItem= new ItemParamItem();
		itemParamItem.setParamData(itemParams);
		itemParamItem.setItemId(item.getId());
		this.itemParamItemService.save(itemParamItem);
		
		
	}
	/**
	 * 查询商品列表
	 * @param page 
	 * @param rows
	 * @return
	 */
	public PageInfo<Item>  querPageyList(Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		
		Example example = new Example(Item.class);
		example.setOrderByClause("updated Desc");
		List<Item>  list=this.itemMapper.selectByExample(example);
		
		return new PageInfo<Item>(list);
		
	}
	/**
	 * 修改商品数据
	 * @param item
	 * @param desc
	 */
	public void updateItem(Item item, String desc,String itemParams) {
		item.setStatus(null);
		item.setCreated(null);
		item.setUpdated(new Date());
		super.updateSelect(item);
		String url=WEB_BASE_URL+"item/cache/"+item.getId()+".html";
		try {
			apiService.doGet(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ItemDesc itemDesc= new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		this.itemDescService.updateSelect(itemDesc);
		/*ItemParamItem itemParamItem= new ItemParamItem();
		itemParamItem.setParamData(itemParams);
		itemParamItem.setId(item.getId());*/
		if(StringUtils.indexOf(itemParams, "v")==-1) {
			return;
		}
		this.itemParamItemService.updateItemParamItem(item.getId(),itemParams);
	}

}
