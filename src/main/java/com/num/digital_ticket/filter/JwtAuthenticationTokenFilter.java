package com.num.digital_ticket.filter;

import com.num.digital_ticket.config.properties.JwtSecurityProperties;
import com.num.digital_ticket.utils.JsonUtil;
import com.num.digital_ticket.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * JWT登录校验过滤器
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private JwtSecurityProperties jwtSecurityProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader(jwtSecurityProperties.getHeader());
        System.out.println(token);
        if (token != null && token.startsWith(jwtSecurityProperties.getTokenStartWith())) {
            token = token.substring(jwtSecurityProperties.getTokenStartWith().length());// The part after "Bearer "

            // 解析token中的username，同时校验token是否合法
            String username = jwtTokenUtil.getUserNameFromToken(token);
            if (StringUtils.isNotBlank(username)
                    && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                UserDetails userDetails = null;
                try {
                    // 从redis获取userInfo
                    String jsonStr = stringRedisTemplate.opsForValue().get(username);
                    // json反序列化
                    userDetails = JsonUtil.JsonConvertUserDetails(jsonStr);
                }catch (NullPointerException e) {
                    // 有合法token，但redis 中没有对应数据
                    // 用户未登录
                    filterChain.doFilter(request, response);
                    return;
                }
                // 用户已登陆，且token校验通过
                // 验证token中的信息是否有效，true：通过验证，false：token过期
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
//                // 刷新token
//                if(jwtTokenUtil.canRefresh(token)) {
//                    String refreshToken = jwtTokenUtil.generateRefreshToken(token);
//                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
