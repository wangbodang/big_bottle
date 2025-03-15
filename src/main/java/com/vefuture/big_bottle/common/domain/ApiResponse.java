package com.vefuture.big_bottle.common.domain;

/**
 * @author wangb
 * @date 2025/3/15
 * @description TODO: 类描述
 */
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

    // 快捷返回成功结果
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "Success", null);
    }

    // 快捷返回成功结果
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    // 快捷返回错误结果
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

}
