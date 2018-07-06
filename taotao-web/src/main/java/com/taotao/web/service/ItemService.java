package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.bean.Item;

@Service
public class ItemService {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	@Autowired
	private ApiService apiService;
	@Value("${TAOTAO_MANAGE_URL}")
	private String TAOTAO_MANAGE_URL;
	@Autowired
	private RedisService redisService;
	
	@Value("${REDIS_WEB_ITEM_KEY_DETAIL}")
	private String REDIS_KEY;
	@Value("${REDIS_WEB_ITEM_KEY_DESC}")
	private String REDIS_KEY_DESC;
	@Value("${REDIS_WEB_ITEM_KEY_PARAM}")
	private String REDIS_KEY_PARAM;
	
	@Value("${REDIS_WEB_ITEM_TIME}")
	private Integer TIME;
	/**
	 * 根据itemid查询item对象
	 * 
	 * @param itemId
	 * @return
	 */
	public Item queryItemByItemId(Long itemId) {
		String key=REDIS_KEY+itemId;
		//从缓存中命中
		try {
			String cacheData=this.redisService.get(key);
			return MAPPER.readValue(cacheData, Item.class);
		}  catch (Exception e) {
			
		}
		
		
		String url = TAOTAO_MANAGE_URL + "rest/item/" + itemId;
		Item item = null;
		String jsonData=null;
		try {
			jsonData= apiService.doGet(url);
			item = MAPPER.readValue(jsonData, Item.class);

		} catch (Exception e) {

			e.printStackTrace();
		}
		try {
			this.redisService.set(key, jsonData, TIME);
		} catch (Exception e) {
			
		}
		return item;
	}

	/**
	 * 根据itemid查询商品描述
	 * 
	 * @param itemId
	 * @return
	 */
	public ItemDesc queryItemDescByItemId(Long itemId) {
		String key=REDIS_KEY_DESC+itemId;
		//从缓存中命中
		try {
			String cacheData=this.redisService.get(key);
			return MAPPER.readValue(cacheData, ItemDesc.class);
		}  catch (Exception e) {
			
		}
		
		
		String url = TAOTAO_MANAGE_URL + "rest/item/desc/" + itemId;
		ItemDesc itemDesc = null;
		String jsonData = null;
		try {
			jsonData = apiService.doGet(url);
			itemDesc = MAPPER.readValue(jsonData, ItemDesc.class);

		} catch (Exception e) {

			e.printStackTrace();
		}
		try {
			this.redisService.set(key, jsonData, TIME);
		} catch (Exception e) {
			
		}
		return itemDesc;
	}

	/**
	 * 根据商品id查询规格参数数据
	 */
	public String queryItemParamByItemid(Long itemId) {
		
		
		
		try {
			String url = TAOTAO_MANAGE_URL + "rest/item/param/item/" + itemId;
			String jsonData = apiService.doGet(url);
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			ArrayNode paramData = (ArrayNode) MAPPER.readTree(jsonNode.get("paramData").asText());
			StringBuilder sb = new StringBuilder();
			sb.append(
					"<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\">\n    <tbody>\\n");
			for (JsonNode param : paramData) {

				sb.append("        <tr>\\n            <th class=\"tdTitle\" colspan=\"2\">" + param.get("group")
						+ "</th>\n         </tr>\\n");
				ArrayNode params = (ArrayNode) param.get("params");
				for (JsonNode p : params) {
					   sb.append("        <tr>\n");
                       sb.append("            <td class=\"tdTitle\">"+p.get("k")+"</td>\n");
                       sb.append("            <td>"+p.get("v")+"</td>\n");
                       sb.append("        </tr>\n");
				}

			}
			 sb.append("    </tbody>\n");
             sb.append("</table>");
             //返回html片段
             
          
             return sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();

		}
		return "";// 返回出错

	}
}
