package com.vefuture.big_bottle.common.aspect;

import com.vefuture.big_bottle.common.annotation.BlacklistCheck;
import com.vefuture.big_bottle.common.exception.BlacklistException;
import com.vefuture.big_bottle.web.vefuture.service.IBlackListService;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangb
 * @date 2025/5/5
 * @description 黑名单拦截切面
 */
@Slf4j
@Aspect
@Component
public class BlacklistCheckAspect {

    @Autowired
    private IBlackListService blackListService;

    //黑名单拦截切面
    @Before("@annotation(blacklistCheck)")
    public void checkBlacklist(JoinPoint joinPoint, BlacklistCheck blacklistCheck) throws IllegalAccessException {
        Object[] args = joinPoint.getArgs(); // 所有方法参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames(); // 参数名列表

        Map<String, Object> flatParamMap = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            if (arg == null) continue;

            // 如果是基本类型（如 String, Integer），直接放入参数 map
            if (isSimpleValueType(arg.getClass())) {
                flatParamMap.put(paramNames[i], arg);
            } else {
                // 是实体类，提取其所有字段（包括 private 字段）
                Field[] fields = arg.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object val = field.get(arg);
                    flatParamMap.put(field.getName(), val);
                }
            }
        }

        for (String param : blacklistCheck.params()) {
            Object val = flatParamMap.get(param);
            if (val != null && blackListService.isBlacklisted(val.toString())) {
                throw new BlacklistException("参数 [" + param + "=" + val + "] 在黑名单中，拒绝访问！");
            }
        }
    }

    /**
     * 判断是否为基础类型（String、Number、Boolean 等）
     */
    private boolean isSimpleValueType(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz == String.class
                || Number.class.isAssignableFrom(clazz)
                || clazz == Boolean.class
                || clazz == Character.class
                || Date.class.isAssignableFrom(clazz);
    }
}
