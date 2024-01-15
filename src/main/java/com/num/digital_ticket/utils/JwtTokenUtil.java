package com.num.digital_ticket.utils;

import com.num.digital_ticket.config.properties.JwtSecurityProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * jwt工具类
 */
@Component
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private final JwtSecurityProperties jwtSecurityProperties;

    public JwtTokenUtil(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }
    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return jwtSecurityProperties.getTokenStartWith() + Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, jwtSecurityProperties.getBase64Secret())
                .compact();
    }
    private String generateToken(Map<String, Object> claims, long expiration) {
        return jwtSecurityProperties.getTokenStartWith() + Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, jwtSecurityProperties.getBase64Secret())
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
//        Claims claims = null;
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecurityProperties.getBase64Secret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            LOGGER.info("JWT格式验证失败:{}",token);
            throw new BadCredentialsException("Invalid Token", e);
        }catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("Token Expired", e);
        }
//        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtSecurityProperties.getAccessTokenExpiration() * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username =  claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return (Objects.equals(username, userDetails.getUsername())) && !isTokenExpired(token);
    }



    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
    public String generateToken(UserDetails userDetails, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims, expiration);
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, jwtSecurityProperties.getAccessTokenExpiration());
    }
    public String generateRefreshToken(UserDetails userDetails) {
//        Claims claims = getClaimsFromToken(token);
//        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(userDetails, jwtSecurityProperties.getRefreshTokenExpiration());
    }

    public String parseTokenForUsername(HttpServletRequest request) {
        // 获取token
        String token = request.getHeader(jwtSecurityProperties.getHeader());
        System.out.println(token);
        if (token != null && token.startsWith(jwtSecurityProperties.getTokenStartWith())) {
            token = token.substring(jwtSecurityProperties.getTokenStartWith().length());// The part after "Bearer "
        }
        // 解析token中的username，同时校验token是否合法
        String username = this.getUserNameFromToken(token);
        return username;
    }
}
