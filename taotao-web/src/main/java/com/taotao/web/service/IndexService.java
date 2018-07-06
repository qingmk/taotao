package com.taotao.web.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.pojo.Content;

@Service
public class IndexService {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	@Autowired
	private ApiService apiService;
	@Value("${TAOTAO_MANAGE_URL}")
	private String TAOTAO_MANAGE_URL;
	@Value("${INDEXAD1_URL}")
	private String INDEXAD1_URL;
	@Value("${INDEXAD2_URL}")
	private String INDEXAD2_URL;

	/**
	 * 查询大广告
	 * 
	 * @return
	 */
	public String queryIndexAd1() {
		String url = TAOTAO_MANAGE_URL + INDEXAD1_URL;
	

		try {
			String jsonData = apiService.doGet(url);
			if (StringUtils.isEmpty(jsonData)) {
				return null;
			}
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Content.class);
			/*List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			ArrayNode rows = (ArrayNode) jsonNode.get("rows");
			for (JsonNode row : rows) {
				Map<String, Object> map = new LinkedHashMap<>();
				map.put("srcB", row.get("pic").asText());
				map.put("height", 240);
				map.put("alt", row.get("title").asText());
				map.put("width", 670);
				map.put("src", row.get("pic").asText());
				map.put("widthB", 550);
				map.put("href", row.get("url").asText());
				map.put("heightB", 240);
				result.add(map);
			}*/
			
			for (Content content  : (ArrayList<Content>) easyUIResult.getRows()) {
				Map<String, Object> map = new LinkedHashMap<>();
				map.put("srcB", content.getPic());
				map.put("height", 240);
				map.put("alt", content.getTitle());
				map.put("width", 670);
				map.put("src", content.getPic());
				map.put("widthB", 550);
				map.put("href", content.getUrl());
				map.put("heightB", 240);
				result.add(map);
			}
			return MAPPER.writeValueAsString(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 查询右侧小广告
	 * 
	 * @return
	 */
	public String queryIndexAd2() {
		String url = TAOTAO_MANAGE_URL + INDEXAD2_URL;
		System.out.println(url + "testtest");

		try {
			String jsonData = apiService.doGet(url);
			if (StringUtils.isEmpty(jsonData)) {
				return null;
			}

			/*JsonNode jsonNode = MAPPER.readTree(jsonData);
			JsonNode row = jsonNode.get("rows").get(0);*/
			
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			ArrayNode rows = (ArrayNode) jsonNode.get("rows");
			for (JsonNode row : rows) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("width", 310);
			map.put("height", 70);
			map.put("src", row.get("pic").asText());
			map.put("href", row.get("url").asText());

			map.put("alt", row.get("title").asText());

			map.put("widthB", 210);

			map.put("heightB", 70);
			map.put("srcB", row.get("pic").asText());
			result.add(map);
			}

			return MAPPER.writeValueAsString(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
