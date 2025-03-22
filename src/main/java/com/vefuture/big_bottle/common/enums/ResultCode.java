package com.vefuture.big_bottle.common.enums;

/**
 * @author wangb
 * @date 2025/3/23
 * @description TODO: 类描述
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源未找到"),
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResultCode fromCode(int code) {
        for (ResultCode value : ResultCode.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return code + " - " + message;
    }
}
