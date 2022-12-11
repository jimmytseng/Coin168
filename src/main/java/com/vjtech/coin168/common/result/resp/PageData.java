package com.vjtech.coin168.common.result.resp;

import com.vjtech.coin168.dto.PageRequest;

public class PageData {
	
	
	private PageRequest pagetRest;
	
	private Integer totalCount;
	
	private Object data;

	public PageRequest getPagetRest() {
		return pagetRest;
	}

	public void setPagetRest(PageRequest pagetRest) {
		this.pagetRest = pagetRest;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	

}
