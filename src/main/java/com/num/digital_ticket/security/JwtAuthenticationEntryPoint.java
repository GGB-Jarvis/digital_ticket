package com.num.digital_ticket.security;

import com.alibaba.fastjson.JSON;
import com.num.digital_ticket.utils.AuthenticateExceptionUtil;
import com.num.digital_ticket.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 * 当未登录或者token失效访问接口时，自定义的返回结果
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        WebUtils.rederString(response, JSON.toJSONString(AuthenticateExceptionUtil.getErrorMsgByExceptionType(authException)));
    }
}
