package com.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.impl.ContentService;

@Controller
@RequestMapping("content")
public class CotentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CotentController.class);
	@Autowired
	private ContentService contentService;
	@RequestMapping(method = RequestMethod.POST)
	/**
	 * 保存内容
	 * @param content
	 * @return
	 */
	public ResponseEntity<Void> saveContent(Content content) {
		try {

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("新增内容,CategoryId={},tite={}",content.getCategoryId(),content.getTitle());
			}
			content.setId(null);
			this.contentService.save(content);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("新增内容成功,CategoryId={}",content.getCategoryId());
			}
			// 成功创建
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			LOGGER.error("新增内容失败,内容名字" + content.getTitle() + ",CategoryId=" + content.getCategoryId(), e);

		}
		// 出现错误
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	/**
	 * 查询内容数据
	 * @param page
	 * @param rows
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<EasyUIResult> queryContent(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "10") Integer rows,@RequestParam("categoryId")long categoryId) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询内容,CategoryId={},",categoryId);
			}
			
			PageInfo<Content> pageInfo=this.contentService.queryList(page,rows,categoryId);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询内容成功,CategoryId={}",categoryId);
			}
			EasyUIResult result= new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
			return ResponseEntity.ok().body(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("查询内容失败,CategoryId=" + categoryId, e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	
	
	
}
