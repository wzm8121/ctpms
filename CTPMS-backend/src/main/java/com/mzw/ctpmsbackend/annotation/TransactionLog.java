package com.mzw.ctpmsbackend.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TransactionLog {
    String value() default "";  // 交易描述
    String type() default "TRADE";  // 交易类型(TRADE/REFUND/TOPUP等)
}