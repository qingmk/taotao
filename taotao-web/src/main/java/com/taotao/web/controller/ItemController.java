package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.bean.Item;
import com.taotao.web.service.ItemService;




@RequestMapping("item")
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	@RequestMapping(value="{itemId}",method=RequestMethod.GET)
	/**
	 * 
	 * @param itemId
	 * @return
	 */
	public ModelAndView showDetails(@PathVariable(value="itemId")Long itemId) {
		
		ModelAndView mvAndView = new ModelAndView();
		mvAndView.setViewName("item");
		Item item= this.itemService.queryItemByItemId(itemId);
		mvAndView.addObject("item", item);
		ItemDesc itemDesc = this.itemService.queryItemDescByItemId(itemId);
		mvAndView.addObject("itemDesc", itemDesc);
		String itemParam = this.itemService.queryItemParamByItemid(itemId);
		mvAndView.addObject("itemParam", itemParam);
		
		return mvAndView;
	}

	

}
