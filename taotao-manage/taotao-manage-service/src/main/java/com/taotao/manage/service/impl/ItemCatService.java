package com.taotao.manage.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatData;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemCat;
@Service
public class ItemCatService extends BaseService<ItemCat> {
	/*@Autowired
	private ItemCatMapper itemCatMapper;*/
	
	/*@Override
	public Mapper<ItemCat> getMapper() {
		
		return itemCatMapper;
	}*/
	@Autowired
	private RedisService redisService;
	@Value("${REDIS_MANAGE_ITEM_CAT_KEY}")
	private String REDIS_KEY;
	@Value("${REDIS_MANAGE_ITEM_CAT_TIME}")
	private Integer REDIS_TIME;
	private static  ObjectMapper MAPPER = new ObjectMapper();
	/**
	 * 全部查询，
	 * @return
	 */
	public ItemCatResult queryAllToTree() {
		ItemCatResult result = new ItemCatResult();
		
		try {
		//从缓存命中
		
		String cacheData =this.redisService.get(REDIS_KEY);
		this.redisService.expire(REDIS_KEY, REDIS_TIME);
		return MAPPER.readValue(cacheData, ItemCatResult.class);
		
		}catch(Exception e) {
			
		}
		
		List<ItemCat> cats = super.queryAll();
		
		// 转为map存储，key为父节点ID，value为数据集合
		Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
		for (ItemCat itemCat : cats) {
			if(!itemCatMap.containsKey(itemCat.getParentId())){
				itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
			}
			itemCatMap.get(itemCat.getParentId()).add(itemCat);
		}
		
		// 封装一级对象
		List<ItemCat> itemCatList1 = itemCatMap.get(0L);
		for (ItemCat itemCat : itemCatList1) {
			ItemCatData itemCatData = new ItemCatData();
			itemCatData.setUrl("/products/" + itemCat.getId());
			itemCatData.setName("<a href='"+itemCatData.getUrl()+"'>"+itemCat.getName()+"</a>");
			result.getItemCats().add(itemCatData);
			if(!itemCat.getIsParent()){
				continue;
			}
			
			// 封装二级对象
			List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
			List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
			itemCatData.setItems(itemCatData2);
			for (ItemCat itemCat2 : itemCatList2) {
				ItemCatData id2 = new ItemCatData();
				id2.setName(itemCat2.getName());
				id2.setUrl("/products/" + itemCat2.getId() );
				itemCatData2.add(id2);
				if(itemCat2.getIsParent()){
					// 封装三级对象
					List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
					List<String> itemCatData3 = new ArrayList<String>();
					id2.setItems(itemCatData3);
					for (ItemCat itemCat3 : itemCatList3) {
						itemCatData3.add("/products/" + itemCat3.getId() + "|"+itemCat3.getName());
					}
				}
			}
			if(result.getItemCats().size() >= 14){
				break;
			}
		}
		try {
			this.redisService.set(REDIS_KEY, MAPPER.writeValueAsString(result),REDIS_TIME);
		} catch (Exception e) {
			
		}
		return result;
	}

}
