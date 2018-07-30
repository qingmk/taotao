package com.taotao.web.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.httpclient.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Order;

@Service
@RequestMapping("order")
public class OrderService {
	@Autowired
	private ApiService apiService;
	
	private ObjectMapper MAPPER = new ObjectMapper();
	@Value("${TAOTAO_ORDER_SUBMITORDER}")
	private String ORDERURL;
	/**
	 * 提交订单
	 * @param order
	 * @return 订单号
	 */
	public String submitOrder(Order order) {
		String url=ORDERURL+"/order/create";
		try {
			HttpResult result = apiService.doPostJson(url, MAPPER.writeValueAsString(order));
			if(result.getCode().intValue()==200) {
				String jsonData=result.getData();
				JsonNode jsonNode=MAPPER.readTree(jsonData);
				if(jsonNode.get("status").intValue()==200) {
					//订单提交成功，返回订单号
					return jsonNode.get("data").asText();
				}
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	
	
	public Order queryOrderByOrderId(String orderId) {
		String url=ORDERURL+"/order/query/"+orderId;
		try {
			String jsonData=apiService.doGet(url);
			if(StringUtils.isNotEmpty(jsonData))
			return MAPPER.readValue(jsonData, Order.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
