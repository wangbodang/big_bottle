package com.vefuture.big_bottle.common.aspect;

import com.vefuture.big_bottle.common.annotation.RequirePermission;
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
import java.util.List;

@Aspect
@Component
@Slf4j
public class RoleCheckAspect {

    private final HttpServletRequest request;

    public RoleCheckAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Before("@annotation(com.vefuture.big_bottle.common.annotation.RequireRole) || @annotation(com.vefuture.big_bottle.common.annotation.RequirePermission)")
    public void checkAccess(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        LoginUser user = (LoginUser) request.getAttribute("loginUser");
        if (user == null) {
            throw new ForbiddenException("未登录，禁止访问");
        }

        if (method.isAnnotationPresent(RequireRole.class)) {
            String[] roles = method.getAnnotation(RequireRole.class).value();
            if (!Arrays.asList(roles).contains(user.getRole())) {
                throw new ForbiddenException("权限不足，当前角色为: " + user.getRole());
            }
        }

        if (method.isAnnotationPresent(RequirePermission.class)) {
            String[] requiredPerms = method.getAnnotation(RequirePermission.class).value();
            List<String> userPerms = user.getPermissions();
            if (userPerms == null || Arrays.stream(requiredPerms).noneMatch(userPerms::contains)) {
                throw new ForbiddenException("缺少必要权限: " + Arrays.toString(requiredPerms));
            }
        }

        log.info("✅ 权限校验通过，用户: {}", user.getUsername());
    }
}
