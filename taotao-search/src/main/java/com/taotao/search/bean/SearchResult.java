package com.taotao.search.bean;

import java.util.List;
/**
 * 搜索返回的数据类型,包含具体数据和总数
 * @author Administrator
 *
 */
public class SearchResult {
	private Long total;
	private List<?> list;
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	public SearchResult(long i, List<?> list) {
		super();
		this.total = i;
		this.list = list;
	}
	public SearchResult() {
		super();
		
	}

}
