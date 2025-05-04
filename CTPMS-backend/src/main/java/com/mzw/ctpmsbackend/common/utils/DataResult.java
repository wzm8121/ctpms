package com.mzw.ctpmsbackend.common.utils;

public class DataResult<T> {
    private int code;
    private String message;
    private T data;

    public DataResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> DataResult<T> success(T data) {
        return new DataResult<>(200, "success", data);
    }

    public static <T> DataResult<T> success(String message) {
        return new DataResult<>(200, message, null);
    }

    public static <T> DataResult<T> success(String message, T data) {
        return new DataResult<>(200, message, data);
    }

    public static <T> DataResult<T> error(String message) {
        return new DataResult<>(500, message, null);
    }

    public static <T> DataResult<T> fail(String message) {
        return new DataResult<>(400, message, null);
    }

    public static <T> DataResult<T> fail(String message, T data) {
        return new DataResult<>(400, message, data);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
