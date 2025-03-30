package com.vefuture.big_bottle.common.aspect;

import com.vefuture.big_bottle.common.annotation.RequireRole;
import com.vefuture.big_bottle.common.exception.ForbiddenException;
import com.vefuture.big_bottle.web.auth.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RoleCheckAspect {

    private final HttpServletRequest request;

    public RoleCheckAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Before("@annotation(com.vefuture.big_bottle.common.annotation.RequireRole)")
    public void checkRole(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RequireRole requireRole = method.getAnnotation(RequireRole.class);
        String[] required = requireRole.value();

        LoginUser user = (LoginUser) request.getAttribute("loginUser");

        if (user == null) {
            throw new RuntimeException("未登录，拒绝访问！");
        }

        String currentRole = user.getRole();
        if (!Arrays.asList(required).contains(currentRole)) {
            throw new ForbiddenException("权限不足，当前角色为：" + currentRole);
        }

        log.info("✅ 权限校验通过，角色为 {}", currentRole);
    }
}
