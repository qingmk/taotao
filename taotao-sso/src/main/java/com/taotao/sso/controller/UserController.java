package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("user/")
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * 跳转注册
	 * 
	 * @return
	 */
	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String toRegister() {
		return "register";
	}

	/**
	 * 跳转登录
	 * 
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String toLogin() {
		return "login";
	}

	/**
	 * 检测数据是否可用
	 * 
	 * @param param
	 *            根据type不同代表用户名，电话号码，邮箱
	 * @param type
	 *            可以为1，2，3，
	 * @return
	 */
	@RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> check(@PathVariable(value = "param") String param,
			@PathVariable(value = "type") Integer type) {
		try {
			Boolean bool = this.userService.check(param, type);
			/**
			 * 为了与前台配合，返回值取反
			 */
			if (null == bool) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			return ResponseEntity.ok(!bool);
		} catch (Exception e) {

		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 注册方法
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "doRegister", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doRegister(@Validated User user, BindingResult bindingResult) {
		Map<String, Object> result = new HashMap<>();
		if (bindingResult.hasErrors()) {
			List<String> list = new ArrayList<>();
			List<ObjectError> errors = bindingResult.getAllErrors();
			for (ObjectError obj : errors) {
				String error = obj.getDefaultMessage();
				list.add(error);
			}

			result.put("status", "300");
			result.put("data", StringUtils.join(list, "|"));
			return result;
		}

		Boolean bool = this.userService.saveUser(user);

		if (bool) {
			result.put("status", "200");
		} else {
			result.put("status", "300");
			result.put("data", " 确认");
		}

		return result;
	}

	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "doLogin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doLogin(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,HttpServletRequest request,HttpServletResponse response) {
		String token = null;
		Map<String, Object> result = new HashMap<>();
		try {
			token = this.userService.doLogin(username, password);
		} catch (JsonProcessingException e) {
			result.put("status", 500);
		}

		if (null == token) {
			result.put("status", 400);

		} else {
			result.put("status", 200);
			// TODO
			// 写入cookie
			CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		}

		return result;
	}
	@RequestMapping(value="{token}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> queryUserByToken(@PathVariable(value="token")String token,@RequestParam(value="callback",required=false) String callback){
		try {
			String jsondata=this.userService.queryUserByToken(token);
			if(null==jsondata) {
				ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}else {
				if(StringUtils.isEmpty(callback)) {
					return ResponseEntity.ok().body(jsondata);
				}
				return ResponseEntity.ok().body(callback+"("+jsondata+");");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}
}
