package com.taotao.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("page")
public class PageController {
	/**
	 * 该类作用是直接访问WEB-INF下的jsp文件
	 * @param pageName
	 * @return
	 */
	@RequestMapping(value="{pageName}",method=RequestMethod.GET)
	public String toPage(@PathVariable("pageName") String pageName) {
		return pageName;
		
	}

	
	
	

}
