package com.num.digital_ticket.security;

import com.alibaba.fastjson.JSON;
import com.num.digital_ticket.config.properties.JwtSecurityProperties;
import com.num.digital_ticket.utils.JwtTokenUtil;
import com.num.digital_ticket.utils.WebUtils;
import com.num.digital_ticket.vo.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义退出处理器
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {
    @Resource
    private JwtSecurityProperties jwtSecurityProperties;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 删除redis中的登录信息
        String token = request.getHeader("token").substring(jwtSecurityProperties.getTokenStartWith().length());

        // 验证token是否合法，并解析username
        // token解析失败会返回null
        String username = jwtTokenUtil.getUserNameFromToken(token);

        if(!StringUtils.isEmpty(username)) {
            // redis 中无数据，返回false
            if(Boolean.FALSE.equals(stringRedisTemplate.delete(username))) {
                WebUtils.rederString(response, JSON.toJSONString(Response.fail("该用户还未登录")));
            }
        }else {
            WebUtils.rederString(response, JSON.toJSONString(Response.fail("退出失败")));
        }
    }
}
