package com.vefuture.big_bottle.common.interceptor;

import com.vefuture.big_bottle.common.util.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangb
 * @date 2025/4/3
 * @description VefutureInterceptor 拦截vechain页面的请求
 */
@Slf4j
@Component
public class VefutureInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("--------------------------------------->>>>>>>>> 经过拦截器:[{}]", this.getClass().getName());
        String uri = request.getRequestURI();
        String ip = IpUtils.getIpAddr(request);
        log.info("[VefutureInterceptor] 请求路径：{}，来源IP：{}", uri, ip);
        log.info("---> 请求经过拦截器:{}", this.getClass().getName());
        //默认返回true
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
