package com.mzw.ctpmsbackend.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public DataResult<String> handleNotLoginException(NotLoginException e) {
        return DataResult.error(401,"访问api失败:" + e.getMessage());
    }

    @ExceptionHandler(NotRoleException.class)
    public DataResult<String> handleNotRoleException(NotRoleException e) {
        return DataResult.error(402,"访问api失败:" + e.getMessage());
    }
}
