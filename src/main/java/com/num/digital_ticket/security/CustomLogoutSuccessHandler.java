package com.num.digital_ticket.security;

import com.alibaba.fastjson.JSON;
import com.num.digital_ticket.vo.Response;
import com.num.digital_ticket.utils.WebUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出成功处理器
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        WebUtils.rederString(response, JSON.toJSONString(Response.success("退出成功")));
    }
}
