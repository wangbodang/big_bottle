package com.vefuture.big_bottle.web.auth.entity;

import lombok.Data;

/**
 * @author wangb
 * @date 2025/4/16
 * @description TODO: 类描述
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}
