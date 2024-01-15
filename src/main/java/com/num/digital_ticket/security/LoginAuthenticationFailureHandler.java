package com.num.digital_ticket.security;


import com.alibaba.fastjson.JSON;
import com.num.digital_ticket.utils.AuthenticateExceptionUtil;
import com.num.digital_ticket.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录认证失败后处理
 **/
@Component
@Slf4j
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("AuthenticationException: ", exception.getMessage());
        WebUtils.rederString(response, JSON.toJSONString(AuthenticateExceptionUtil.getErrorMsgByExceptionType(exception)));
    }
}
