package com.vefuture.big_bottle.web.auth.controller;

// AuthController.java
import com.vefuture.big_bottle.common.util.JwtUtil;
import com.vefuture.big_bottle.web.auth.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangb
 * @date 2025/3/30
 * @description 验证
 */
@Slf4j
@RestController
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();
        if ("admin".equals(username) && "123456".equals(password)) {

            // 模拟从数据库查出的用户信息
            LoginUser user = new LoginUser(
                    1001L,
                    "admin",
                    "admin",
                    Arrays.asList("read", "write", "delete"),
                    "admin@example.com",
                    true
            );

            String token = jwtUtil.generateToken(user);
            result.put("code", 200);
            result.put("msg", "login success");
            result.put("token", token);
            return result;
        }

        result.put("code", 401);
        result.put("msg", "Invalid credentials");
        return result;
    }
}
