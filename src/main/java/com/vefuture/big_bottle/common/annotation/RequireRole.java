package com.vefuture.big_bottle.common.annotation;

/**
 * @author wangb
 * @date 2025/3/30
 * @description TODO: 类描述
 */
import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    //todo 可以用数组
    String[] value(); // 角色名称，如 "admin"
}