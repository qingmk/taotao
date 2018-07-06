package com.taotao.manage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.impl.ContentCategoryService;

@Controller
@RequestMapping("content/category")
public class CotentCategoryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CotentCategoryController.class);
	@Autowired
	private ContentCategoryService contentCategoryService;

	/**
	 * 通过父节点ID查询内容分类
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ContentCategory>> queryByCategroyId(
			@RequestParam(value = "id", defaultValue = "0") Long parentId) {
		try {
			ContentCategory record = new ContentCategory();
			record.setParentId(parentId);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始查询内容分类,parentid=" + parentId);
			}
			List<ContentCategory> result = this.contentCategoryService.queryListBYWhere(record);
			if (null == result) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("查询内容为空,parentid=" + parentId);
				}
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询内容成功,reslut=" + result.get(0).getText());
			}
			return ResponseEntity.ok().body(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("查询内容出错,parentid=" + parentId, e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	/**
	 * 新增内容分类子节点
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory) {

		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始保存内容分类,parentid=" + contentCategory.getParentId());
			}
			this.contentCategoryService.saveCOntentCategory(contentCategory);

		
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("保存内容分类成功,content=" + contentCategory.getText());
			}
			
			
			return ResponseEntity.ok().body(contentCategory);
		} catch (Exception e) {
			LOGGER.error("保存内容出错,parentid=" + contentCategory.getParentId(), e);
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

	}
	/**
	 * 重命名节点
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> updateContentCategory(ContentCategory contentCategory) {

		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始修改内容分类,parentid=" + contentCategory.getParentId());
			}
			this.contentCategoryService.updateSelect(contentCategory);

		
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("修改内容分类成功,content=" + contentCategory.getText());
			}
			
			
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			LOGGER.error("修改内容出错,parentid=" + contentCategory.getParentId(), e);
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

	}

	/**
	 * 删除内容分类节点
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteContentCategory(ContentCategory contentCategory) {

		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始删除内容分类,parentid=" + contentCategory.getParentId());
			}
			this.contentCategoryService.deleteContentCategory(contentCategory);

		
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("删除内容分类成功,content=" + contentCategory.getText());
			}
			
			
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			LOGGER.error("删除内容出错,parentid=" + contentCategory.getParentId(), e);
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

	}
	
	
	
}
