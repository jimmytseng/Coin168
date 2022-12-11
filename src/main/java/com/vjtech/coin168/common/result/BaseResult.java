package com.vjtech.coin168.common.result;

import com.vjtech.coin168.common.result.CommonEnum;
/**
 * Title: 返回结果集 Description: Copyright: Copyright (c) das.vjteck 2021 Company:
 * vjteck
 *
 * @author jimmytseng
 * @version v1.0
 * @date 2018-07-02
 */
public class BaseResult {

	private int code;

	private String message;

	private Object data;

	public BaseResult(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public BaseResult(CommonEnum resultEnum, Object data) {
		this(resultEnum.getCode(), resultEnum.getMessage(), data);
	}
	
	public BaseResult(CommonEnum resultEnum) {
		this(resultEnum.getCode(), resultEnum.getMessage(), null);
	}
	
    public BaseResult(CommonEnum resultEnum, String message) {
        this(resultEnum.getCode(), message, null);
    }
    
	public BaseResult(int code, String message) {
		this(code, message, null);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
