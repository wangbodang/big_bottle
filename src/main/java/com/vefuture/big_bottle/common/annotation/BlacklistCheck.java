package com.vefuture.big_bottle.common.annotation;

import java.lang.annotation.*;

/**
 * @author wangb
 * @date 2025/5/5
 * @description 该注解拦截在黑名单里的钱包地址 名称为 walletAddress 或 wallet_address
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BlacklistCheck {
    String[] params(); // 要校验的参数名列表
}
