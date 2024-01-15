package com.num.digital_ticket.aspect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiOperationLog {
    /**
     * 日志方法描述
     *
     * @return
     */
    String description() default "";

}

