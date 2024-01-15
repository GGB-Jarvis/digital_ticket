package com.num.digital_ticket.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常
 **/
@Getter
@Setter
public class BizException extends RuntimeException {
    // 错误码
    private String errorCode;
    // 错误信息
    private String errorMessage;

    public BizException(BaseExceptionInterface exception) {
        this.errorCode = exception.getErrorCode();
        this.errorMessage = exception.getErrorMessage();
    }

}
