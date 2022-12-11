package com.vjtech.coin168.configuration;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Title: 配置跨域處理
 * Description:
 * Copyright: Copyright (c) das.com 2018
 * Company: vjteck
 *
 * @author jimmytseng
 * @version v1.0
 * @date 2021-07-13
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CrosFilterConfiguration implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        String origin = req.getHeader("Origin");
        if (!org.springframework.util.StringUtils.isEmpty(origin)) {
            res.addHeader("Access-Control-Allow-Origin", origin);
        }
        res.addHeader("Access-Control-Allow-Methods", "*");
        String headers = req.getHeader("Access-Control-Request-Headers");
        if (!org.springframework.util.StringUtils.isEmpty(headers)) {
            res.addHeader("Access-Control-Allow-Headers", headers);
        }
        res.addHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
