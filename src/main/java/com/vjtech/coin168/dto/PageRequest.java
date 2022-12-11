package com.vjtech.coin168.dto;

import org.apache.commons.lang3.StringUtils;

public class PageRequest {
	
	private int count ;
	
    private int pageNumber;
    
    private String qryCoin;
    
    private String orderColumn;
    
    private String orderType;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

    public String getQryCoin() {
        return qryCoin;
    }

    public void setQryCoin(String qryCoin) {
        this.qryCoin = qryCoin;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderType() {
        if (StringUtils.isBlank(this.orderType)) {
            return null;
        }
        if (StringUtils.equals("descending", this.orderType)) {
            return "DESC";
        }else {
            return "ASC";
        }
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }


}
