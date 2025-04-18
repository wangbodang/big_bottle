package com.vefuture.big_bottle.web.auth.controller;

// AuthController.java

import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.util.JwtTokenUtil;
import com.vefuture.big_bottle.web.auth.entity.LoginRequest;
import com.vefuture.big_bottle.web.auth.entity.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangb
 * @date 2025/3/30
 * @description 验证
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("--->  loginRequest:[{}]", loginRequest);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenUtil.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/checkToken")
    public ApiResponse<?> checkToken(HttpServletRequest request) {
        log.info("---> 到达检查接口");
        String token = resolveToken(request);
        if (StringUtils.hasText(token) && jwtTokenUtil.validateToken(token)) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            return ApiResponse.success("Token 有效，用户：" + username);
        } else {
            return ApiResponse.unauthorized("Token 无效或已过期");
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            log.info("---> Token:[{}]", token);
            return token;
        }
        return null;
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