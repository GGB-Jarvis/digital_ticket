package com.num.digital_ticket.security;

import com.alibaba.fastjson.JSON;
import com.num.digital_ticket.utils.AuthenticateExceptionUtil;
import com.num.digital_ticket.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问拒绝处理器，无权限访问
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("暂无权限");
        WebUtils.rederString(response, JSON.toJSONString(AuthenticateExceptionUtil.getErrorMsgByExceptionType(accessDeniedException)));
    }
}
