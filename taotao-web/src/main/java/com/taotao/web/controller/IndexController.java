package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.IndexService;

@Controller
public class IndexController {
	@Autowired
	private IndexService indexService;
	@RequestMapping(method=RequestMethod.GET,value="index")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("index");
		String StringAd1=this.indexService.queryIndexAd1();
		mv.addObject("indexAd1", StringAd1);
		
		String StringAd2=this.indexService.queryIndexAd2();
		mv.addObject("indexAd2", StringAd2);
		return mv;
	}
	
	
}
