package com.num.digital_ticket.utils;

import com.num.digital_ticket.enums.ResponseCodeEnum;
import com.num.digital_ticket.vo.Response;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.csrf.CsrfException;


/**
 * 认证异常工具类
 */
public class AuthenticateExceptionUtil {

    public static Response getErrorMsgByExceptionType(AuthenticationException e) {
        if(e instanceof LockedException) {
            return Response.fail(ResponseCodeEnum.ACCOUNT_LOCKED);
        } else if (e instanceof CredentialsExpiredException || e instanceof BadCredentialsException) {
            return Response.fail(ResponseCodeEnum.USERNAME_OR_PWD_ERROR);
        } else if (e instanceof InsufficientAuthenticationException) {
            return Response.fail(ResponseCodeEnum.UNAUTHORIZED);
        } else if (e instanceof AccountExpiredException) {
            return Response.fail(ResponseCodeEnum.ACCOUNT_EXPIRED);
        } else if (e instanceof DisabledException) {
            return Response.fail(ResponseCodeEnum.ACCOUNT_DISABLED);
        } else if (e instanceof AuthenticationServiceException) {
            return Response.fail(ResponseCodeEnum.LOGIN_FAIL);
        }
        return Response.fail(e.getMessage());
    }

    public static Response getErrorMsgByExceptionType(AccessDeniedException e) {
        if (e instanceof CsrfException) {
            return Response.fail(ResponseCodeEnum.CSRF_EXCEPTION);
        } else if (e instanceof AuthorizationServiceException) {
            return Response.fail(ResponseCodeEnum.AUTHORIZATION_SERVICE_EXCEPTION);
        } else if (e instanceof AccessDeniedException) {
            return Response.fail(ResponseCodeEnum.UNAUTHORIZED);
        }
        return Response.fail(e.getMessage());
    }
}
