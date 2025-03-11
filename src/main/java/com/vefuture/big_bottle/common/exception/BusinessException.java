package com.vefuture.big_bottle.common.exception;


public class BusinessException extends RuntimeException {

    private int code;

    /**
     * 仅包含错误信息，使用默认错误码（例如400）。
     */
    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    /**
     * 同时指定错误码和错误信息。
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 包含错误信息和异常原因，使用默认错误码（例如400）。
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 400;
    }

    /**
     * 同时指定错误码、错误信息和异常原因。
     */
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

