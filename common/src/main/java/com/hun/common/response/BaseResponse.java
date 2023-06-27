package com.hun.common.response;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private Integer code;
    private String message;
    private T data;

    private BaseResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <R> BaseResponse<R> ok() {
        return new BaseResponse<>(200, "ok", null);
    }
    public static <R> BaseResponse<R> ok(R data) {
        return new BaseResponse<>(200, "ok", data);
    }

    public static <R> BaseResponse<R> ok(String message, R data) {
        return new BaseResponse<>(200, message, data);
    }

}
