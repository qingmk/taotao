package com.taotao.search.mq.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.ItemService;
/**
 * 消息队列处理器
 * @author Administrator
 *
 */
public class ItemMQHandler {
	@Autowired
	private HttpSolrServer solrServer;
	private final ObjectMapper MAPPER = new ObjectMapper();
	@Autowired
	private ItemService itemService;
	/**
	 * 具体处理方法
	 * @param msg 要处理的消息
	 */
	public void execute(String msg) {
		try {
			JsonNode node = MAPPER.readTree(msg);
			Long itemId = node.get("itemId").asLong();
			String type=node.get("type").asText();
			if(StringUtils.equals("insert", type)||StringUtils.equals("update", type)) {
				Item item =itemService.queryItemByItemId(itemId);
				if(item!=null) {
					solrServer.addBean(item);
					solrServer.commit();
				}
			}else if(StringUtils.equals("delete", type)) {
				solrServer.deleteById(itemId.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
