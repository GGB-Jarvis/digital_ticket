package com.num.digital_ticket.security;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.num.digital_ticket.entity.RedefinedUserDetails;
import com.num.digital_ticket.mapper.UserMapper;
import com.num.digital_ticket.sevice.impl.UserServiceImpl;
import com.num.digital_ticket.vo.Response;
import com.num.digital_ticket.utils.JwtTokenUtil;
import com.num.digital_ticket.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录认证成功后处理
 */
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserServiceImpl userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Value("${jwt.access-token-expiration}")
    private long expiredTime;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        RedefinedUserDetails userDetails = (RedefinedUserDetails) authentication.getPrincipal();

        // 生成 token
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(userDetails);
        // 将用户信息存入redis
        stringRedisTemplate.opsForValue().set(userDetails.getUsername(), jsonStr);

        Map<String, Object> map = new HashMap<>();
        map.put("access-token", accessToken);
        map.put("refresh-token", refreshToken);
        map.put("expired_time", String.valueOf(new Date(System.currentTimeMillis() + expiredTime * 1000)));
        map.put("user_info", userDetails.getUser());
        // 返回结果
        WebUtils.rederString(response, JSON.toJSONString(Response.success(map)));
    }
}
