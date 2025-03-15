package com.vefuture.big_bottle.common.domain;

import lombok.Data;

// 自定义统一返回结构（也可以使用自己项目中的统一返回类）
@Data
public class RetResponse<T> {
    private int code;
    private String message;
    private T data;
    // 其他字段：如时间戳、异常详情等

    public RetResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }
    public RetResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    // getter/setter 略
}
