package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.search.bean.SearchResult;
import com.taotao.search.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	@RequestMapping(value="search",method=RequestMethod.GET)
	/**
	 * 查询数据并返回视图
	 * @param keywords 查找关键字
	 * @param page 第几页
	 * @return
	 */
	public ModelAndView search(@RequestParam("q")String keywords,@RequestParam(value="page",defaultValue="1")Integer page) {
		ModelAndView mv= new ModelAndView();
		SearchResult result=null;
		
		try {
			keywords=new String(keywords.getBytes("ISO-8859-1"), "utf-8");
			result=this.searchService.search(keywords, page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mv.setViewName("search");
		mv.addObject("query", keywords);
		mv.addObject("itemList", result.getList());
		mv.addObject("page", page);
		int total=result.getTotal().intValue();
		int pages=total%32==0?total/32:total/32+1;
		mv.addObject("pages", pages);
		
		
		
		return mv;
	}

}
