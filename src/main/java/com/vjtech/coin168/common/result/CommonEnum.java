package com.vjtech.coin168.common.result;

/**
 * Title: 结果集
 * Description:
 * Copyright: Copyright (c) vjteck.com 2121
 * Company: vjteck
 *
 * @author jimmytseng
 * @version v1.0
 * @date 2018-07-02
 */
public enum CommonEnum {
    
    SUCCESS(0, "success"),

    CANCEL_VOTE_SUCCESS(1, "cancel vote success"),
    
    FAILED(40000, "failed"),
    
    PARAM_NOT_NULL(40001,"failed"),
    
    PARAM_FORMAT_ERROR(40002,"failed"),
    
    PERMISSION_DENIED(40003, "Permission  denied"),

    ALREADY_EXISTED(40004,"already existed"),

    ACCOUNT_NOT_ENABLE(40005,"Account is not enabled"),

    UNEXPECTED_ERROR(49999,"UNEXPECTED _ERROR");
    
    public int code;
    
    public String message;

    CommonEnum(int code, String message) {
        this.code = code;
        this.message = message;
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

}
