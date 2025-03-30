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
/*
// ✅ 8. 登录失败次数限制（简单缓存控制）
// 在登录逻辑里添加如下伪代码逻辑：
// 使用 Redis 或 ConcurrentHashMap 模拟：Map<String, Integer> loginFailMap;
//
// if (loginFailMap.getOrDefault(username, 0) > MAX_RETRY) {
//     throw new BusinessException("登录失败次数过多，请稍后再试");
// }
//
// 登录失败时 loginFailMap.put(username, failCount++);
// 登录成功时 loginFailMap.remove(username);


// ✅ 9. JWT 自动刷新（伪逻辑）：
// 在拦截器或过滤器中判断 token 剩余时间
// if (token 剩余有效期 < 某阈值) {
//    自动生成新 token 并返回给前端（加到响应头 or 响应体）
// }


// ✅ 10. 国际化 i18n 提示（使用 messages.properties）
// 配置：Spring 的 MessageSource + localeResolver
// 使用方式：messageSource.getMessage("auth.forbidden", null, locale);
// 在 ApiResponse 中返回国际化后的 msg 文本

// 示例 messages.properties
// auth.forbidden = 权限不足
// auth.unauthorized = 未登录或登录过期
// auth.tooManyAttempts = 登录失败次数过多，请稍后再试

*/