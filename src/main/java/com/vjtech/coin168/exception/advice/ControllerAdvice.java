package com.vjtech.coin168.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vjtech.coin168.common.result.BaseResult;
import com.vjtech.coin168.common.result.CommonEnum;
import com.vjtech.coin168.exception.InfoException;

import java.io.UnsupportedEncodingException;

/**
 * Title: 统一异常处理
 * Description:
 * Copyright: Copyright (c) das.com 2018
 * Company: DAS
 *
 * @author zhangXi
 * @version v1.0
 * @date 2018-07-04
 */
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(InvalidGrantException.class)
    public BaseResult exceptionHandler(InvalidGrantException e) {
        return new BaseResult(401, "帐号或密码错误");
    }
    
    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResult exceptionHandler(InvalidTokenException e) {
        return new BaseResult(401, "Unauthorized");
    }
    
    @ExceptionHandler(UnauthorizedUserException.class)
    public BaseResult exceptionHandler(UnauthorizedUserException e) {
        return new BaseResult(401, "Unauthorized");
    }

    @ExceptionHandler(InfoException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResult infoException(InfoException e) {
        return new BaseResult(CommonEnum.UNEXPECTED_ERROR, e.getMessage());
    }

}
