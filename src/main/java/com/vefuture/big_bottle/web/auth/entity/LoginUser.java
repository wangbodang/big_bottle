package com.vefuture.big_bottle.web.auth.entity;

/**
 * @author wangb
 * @date 2025/3/30
 * @description TODO: 类描述
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    private Long uid;
    private String username;
    private String role;
    private List<String> permissions;
    private String email;
    private Boolean isAdmin;
}
