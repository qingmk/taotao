package com.taotao.cart.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.User;
import com.taotao.common.service.ApiService;
@Service
public class UserService {
	@Autowired
	private ApiService apiService;
	
	private ObjectMapper MAPPER=new ObjectMapper();
	
	public User queryUserByToken(String token) {
		String url="http://sso.taotao.com/service/user/"+token;
		try {
			String jsonData=apiService.doGet(url);
			if(StringUtils.isNoneEmpty(jsonData)) {
				return MAPPER.readValue(jsonData, User.class);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	

}
