package com.vefuture.big_bottle.common.annotation;

import java.lang.annotation.*;

/**
 * @author wangb
 * @date 2025/3/31
 * @description TODO: 类描述
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    String[] value(); // 支持多个权限编码
}