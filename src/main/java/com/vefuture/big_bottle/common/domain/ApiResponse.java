package com.vefuture.big_bottle.common.domain;

/**
 * @author wangb
 * @date 2025/3/15
 * @description TODO: 类描述
 */
import cn.hutool.core.util.StrUtil;
import com.vefuture.big_bottle.common.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ApiResponse<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    public static ApiResponse<?> forbidden(String msg) {
        return error(ResultCode.FORBIDDEN.getCode(), msg);
    }

    public static ApiResponse<?> unauthorized(String msg) {
        return error(ResultCode.UNAUTHORIZED.getCode(), msg);
    }

    public static ApiResponse<?> serverError(String msg) {
        return error(500, msg);
    }
    // 快捷返回成功结果
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> ApiResponse<T> success(Integer code, String msg, T data) {
        if(StrUtil.isNotBlank(msg)){
            return new ApiResponse<>(ResultCode.SUCCESS.getCode(), msg, data);
        }
        return new ApiResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    // 快捷返回成功结果
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    // 快捷返回错误结果
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

}
