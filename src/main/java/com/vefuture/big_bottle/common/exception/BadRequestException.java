package com.vefuture.big_bottle.common.exception;

/**
 * @author wangb
 * @date 2025/4/12
 * @description TODO: 类描述
 */
public class BadRequestException extends RuntimeException{

    private final int code;

    public BadRequestException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
