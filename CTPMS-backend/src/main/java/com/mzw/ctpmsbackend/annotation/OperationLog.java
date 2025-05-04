package com.mzw.ctpmsbackend.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    String value() default "";  // 操作描述
    String type() default "SYSTEM";  // 操作类型
}