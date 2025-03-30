package com.vefuture.big_bottle.common.interceptor;

import com.vefuture.big_bottle.common.util.JwtUtil;
import com.vefuture.big_bottle.web.auth.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        log.info("===> Authorization Header: {}", token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                if (jwtUtil.validateToken(token)) {
                    LoginUser user = jwtUtil.getLoginUser(token);
                    request.setAttribute("loginUser", user);
                    log.info("✅ 认证成功，当前用户: {}", user.getUsername());
                    return true;
                }
            } catch (Exception e) {
                log.warn("❌ Token 校验失败", e);
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"msg\":\"Invalid or missing token\"}");
        return false;
    }
}
