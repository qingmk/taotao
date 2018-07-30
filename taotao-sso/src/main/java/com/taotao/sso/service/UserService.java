package com.taotao.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RedisService redisService;
	private ObjectMapper MAPPER=new ObjectMapper();
	/**
	 * 校验参数是否可用
	 * @param param
	 * @param type
	 * @return
	 */
	public Boolean check(String param, Integer type) {
		if (type < 1 || type > 3) {
			return null;
		}
		User user = new User();
		switch (type) {
		case 1:
			user.setUsername(param);
			break;
		case 2:
			user.setPhone(param);
			break;
		case 3:
			user.setEmail(param);
			break;

		default:
			break;
		}
		
		return  this.userMapper.selectOne(user) == null;
	}
	/**
	 * 注册功能
	 * @param user
	 * @return
	 */
	public Boolean saveUser(User user) {
		user.setId(null);
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		return this.userMapper.insert(user)==1;
	}
	/**
	 * 登录功能
	 * @param username
	 * @param password
	 * @return
	 * @throws JsonProcessingException
	 */
	public String doLogin(String username, String password) throws JsonProcessingException {
		
		User record = new User();
		record.setUsername(username);
		User result=this.userMapper.selectOne(record);
		if(null==result) {
			return null;
		}
		//对比密码是否正确
		if(!StringUtils.equals(result.getPassword(), DigestUtils.md5Hex(password))) {
			return null;
		}
		//登录成功
		String token=DigestUtils.md5Hex(System.currentTimeMillis()+username);
		//cookie存入缓存
		this.redisService.set("TOKEN"+token,MAPPER.writeValueAsString(result), 60*30);
		return token;
	}
	public String queryUserByToken(String token) {
	
		String key="TOKEN"+token;
		String jsonData=redisService.get(key);
		if(StringUtils.isEmpty(jsonData)) {
			return null;
		}
		//修改剩余时间
		redisService.set(key, jsonData, 60*30);
		return jsonData;
		
	}

}
