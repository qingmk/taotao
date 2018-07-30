package com.taotao.web.handlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.web.bean.User;
import com.taotao.web.service.UserService;
import com.taotao.web.threadLocal.UserThreadLocal;

public class UserLoginHandlerInterceptor implements HandlerInterceptor{
	
	
	
	
	private  String url="http://sso.taotao.com/user/login.html";
	@Autowired
	private UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//清空当前线程user对象
		UserThreadLocal.set(null);
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		if(StringUtils.isEmpty(token)) {
			response.sendRedirect(url);
			return false;
		}
		User user=this.userService.queryUserByToken(token);
		if(null==user) {
			response.sendRedirect(url);
			return false;
		}
		UserThreadLocal.set(user);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
