package com.vefuture.big_bottle.common.exception;

/**
 * @author wangb
 * @date 2025/5/5
 * @description 参数校验异常
 */
public class BlacklistException extends RuntimeException{
    public BlacklistException(String message) {
        super(message);
    }
}
