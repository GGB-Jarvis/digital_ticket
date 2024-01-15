package com.num.digital_ticket.config;

import com.num.digital_ticket.filter.JwtAuthenticationTokenFilter;
import com.num.digital_ticket.security.*;
import com.num.digital_ticket.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    // 权限不足处理器
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    // 认证失败处理器
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    // 登录成功处理器
    private final LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;
    // 登录失败处理器
    private final LoginAuthenticationFailureHandler loginAuthenticationFailureHandler;
    // 退出登录成功处理器
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    // 退出登录处理器
    private final CustomLogoutHandler customLogoutHandler;
    // 用户信息加载服务 loadUsernameAndPassword(...)
    private final UserService userServiceImpl;
//    @Resource
//    private UserService userServiceImpl;

    @Autowired
    public SecurityConfig(JwtAccessDeniedHandler jwtAccessDeniedHandler,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler,
                          LoginAuthenticationFailureHandler loginAuthenticationFailureHandler,
                          CustomLogoutSuccessHandler customLogoutSuccessHandler,
                          CustomLogoutHandler customLogoutHandler,
                          UserService userServiceImpl
    ) {
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.loginAuthenticationSuccessHandler = loginAuthenticationSuccessHandler;
        this.loginAuthenticationFailureHandler = loginAuthenticationFailureHandler;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
        this.customLogoutHandler = customLogoutHandler;
        this.userServiceImpl = userServiceImpl;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin()
                        .successHandler(loginAuthenticationSuccessHandler)
                        .failureHandler(loginAuthenticationFailureHandler);

        httpSecurity.cors();

        httpSecurity.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
                                "/",
                                "/*.html",
                                "/favicon.ico",
                                "/**/*.html",
                                "/**/*.css",
                                "/**/*.js",
                                "/swagger-resources/**",
                                "/v2/api-docs/**"
                        ).permitAll()
                        .antMatchers("/login","/users/register").permitAll()
                        .anyRequest()
                        .authenticated()
                );
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        httpSecurity.userDetailsService(userServiceImpl);

        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)    //   无权限访问处理器
                .authenticationEntryPoint(jwtAuthenticationEntryPoint); // 认证失败处理器

        httpSecurity.logout()
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .addLogoutHandler(customLogoutHandler);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT登录校验过滤器
     * @return
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
