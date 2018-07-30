package com.taotao.manage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
	private RabbitTemplate rabbitTemplate;
	
	private final ObjectMapper MAPPER=new ObjectMapper();

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
		sendMessage(item.getId(), "insert");
		
	}
	/**
	 * 查询商品列表
	 * @param page 
	 * @param rows
	 * @return
	 */
	public PageInfo<Item>  queryPageyList(Integer page, Integer rows) {
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
		
		ItemDesc itemDesc= new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		this.itemDescService.updateSelect(itemDesc);
		/**
		 * 通知其他系统，商品已经更新
		 * url:要通知的系统de路径
		 * doget:通知其他系统的方法
		 */
		/*String url=WEB_BASE_URL+"item/cache/"+item.getId()+".html";
		try {
			apiService.doGet(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		sendMessage(item.getId(), "update");
		/*ItemParamItem itemParamItem= new ItemParamItem();
		itemParamItem.setParamData(itemParams);
		itemParamItem.setId(item.getId());*/
		if(StringUtils.indexOf(itemParams, "v")==-1) {
			return;
		}
		this.itemParamItemService.updateItemParamItem(item.getId(),itemParams);
		
	
	
	
	}
	private void sendMessage(Long itemId,String type) {
		Map<String,Object> msg = new HashMap<>();
		msg.put("itemId", itemId);
		msg.put("type", type);
		msg.put("date", System.currentTimeMillis());
		try {
			this.rabbitTemplate.convertAndSend("item."+type, MAPPER.writeValueAsString(msg));
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
