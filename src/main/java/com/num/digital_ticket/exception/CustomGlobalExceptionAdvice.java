package com.num.digital_ticket.exception;

import com.num.digital_ticket.enums.ResponseCodeEnum;
import com.num.digital_ticket.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 定义全局异常
 * 捕获全局处理控制器里的异常
 */
@RestControllerAdvice
@Slf4j
public class CustomGlobalExceptionAdvice {

    @ExceptionHandler({ AccessDeniedException.class })
    public void throwAccessDeniedException(AccessDeniedException e) throws AccessDeniedException {
        log.info("============= 捕获到 AccessDeniedException");
        throw e;
    }

    /**
     * 业务异常
     * @return
     */
    @ExceptionHandler({ BizException.class })
    @ResponseBody
    public Response<Object> handleException(HttpServletRequest request, BizException e) {
        log.error("{} request error, errorCode: {}, errorMessage: {}", request.getRequestURI(), e.getErrorCode(), e.getErrorMessage());
        return Response.fail(e);
    }

    /**
     * 参数校验异常
     * @return
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseBody
    public Response<Object> methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        String errorCode = ResponseCodeEnum.PARAM_ERROR.getErrorCode();
        BindingResult result = e.getBindingResult();
        StringBuilder sb = new StringBuilder();

        Optional.ofNullable(result.getFieldErrors()).ifPresent(errors -> {
            errors.forEach(error -> {
                sb.append(error.getField())
                        .append(" ")
                        .append(error.getDefaultMessage())
                        .append(", 当前值: '")
                        .append(error.getRejectedValue())
                        .append("'; ");
            });
        });

        String message = sb.toString();

        log.error("{} request error, errorCode: {}, errorMessage: {}", request.getRequestURI(), errorCode, message);

        return Response.fail(errorCode, message);
    }

    // /**
    //  * 401
    //  * @return
    //  */
    // @ExceptionHandler({ AuthenticationException.class })
    // @ResponseBody
    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // public Response<Object> handleException(HttpServletRequest request, AuthenticationException e) {
    //     log.error("{} request error, ", request.getRequestURI(), e);
    //     return Response.fail(ResponseCodeEnum.UNAUTHORIZED);
    // }

    /**
     * 数据完整性，缺失必要字段
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseBody
    public Response<Object> DataIntegrityViolationHandler(HttpServletRequest request, Exception e) {
        log.error("{} request error, ", request.getRequestURI(), e);
        return Response.fail("缺少必要参数");
    }


    /**
     * 其他异常
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response<Object> handleException(HttpServletRequest request, Exception e) {
        log.error("{} request error, ", request.getRequestURI(), e);
        if("库存不足".equals(e.getMessage())) {
            return Response.fail("库存不足");
        }
        return Response.fail(ResponseCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 字段值（唯一索引的键）重复
     * @return
     */
    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseBody
    public Response<Object> duplicateKeyExceptionhandler(HttpServletRequest request, Exception e) {
        log.error("{} request error, ", request.getRequestURI(), e);
        return Response.fail(ResponseCodeEnum.REGISTER_FAIL);
    }
}
