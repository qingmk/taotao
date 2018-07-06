package com.taotao.manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.ContentCategory;

@Service
public class ContentCategoryService extends BaseService<ContentCategory> {
	/**
	 * 保存内容分类节点
	 * 
	 * @param contentCategory
	 */
	public void saveCOntentCategory(ContentCategory contentCategory) {
		// TODO Auto-generated method stub
		contentCategory.setId(null);
		contentCategory.setIsParent(false);
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		super.save(contentCategory);
		ContentCategory result = super.queryByid(contentCategory.getParentId());
		if (!result.getIsParent()) {
			result.setIsParent(true);
			super.updateSelect(result);
		}

	}

	/**
	 * 删除内容分类节点
	 * 
	 * @param contentCategory
	 */

	public void deleteContentCategory(ContentCategory contentCategory) {
		// TODO Auto-generated method stub
		List<Object> ids = new ArrayList<Object>();
		ids.add(contentCategory.getId());
		findAllSubNode(contentCategory.getId(), ids);
		this.deleteByids(ContentCategory.class, "id", ids);
		ContentCategory record = new ContentCategory();
		record.setParentId(contentCategory.getParentId());
		List<ContentCategory> list = this.queryListBYWhere(record);
		if(null==list||list.isEmpty()) {
			ContentCategory record2 = new ContentCategory();
			record2.setId(contentCategory.getParentId());
			record2.setIsParent(false);
			this.updateSelect(record2);
		}
	
	}
	/**
	 * 获取所有要删除的节点
	 * @param parentid
	 * @param ids
	 */
	private void findAllSubNode(Long parentid, List<Object> ids) {
		ContentCategory record = new ContentCategory();
		record.setParentId(parentid);
		List<ContentCategory> list = this.queryListBYWhere(record);
		for(ContentCategory contentCategory : list) {
			ids.add(contentCategory.getId());
			if(contentCategory.getIsParent()) {
				findAllSubNode(contentCategory.getId(), ids);
			}
		}
	
	}

}
