package com.mzw.ctpmsbackend.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {
    String value() default "";         // 操作说明，如“审核通过”、“驳回”
    String type() default "ITEM";      // 审核类型：USER, ITEM, ORDER
    long targetId() default -1;        // 目标ID（可选）
}
