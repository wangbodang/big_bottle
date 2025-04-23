package com.vefuture.big_bottle.common.config;

// WebConfig.java
import com.vefuture.big_bottle.common.interceptor.AccessLogInterceptor;
import com.vefuture.big_bottle.common.interceptor.JwtInterceptor;
import com.vefuture.big_bottle.common.interceptor.VefutureInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
/**
 * @author wangb
 * @date 2025/3/30
 * @description TODO: 类描述
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Autowired
    private VefutureInterceptor vefutureInterceptor;
    @Autowired
    private AccessLogInterceptor accessLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有的请求, 记录日志
        registry.addInterceptor(accessLogInterceptor)
                .addPathPatterns("/**");
        registry.addInterceptor(jwtInterceptor)
                //.addPathPatterns("/**")  // 默认拦所有
                //.addPathPatterns("/api/**", "/employee/**")
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/login",
                        "/register",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/error"
                );
        // 第二个拦截器：Vefuture 拦截器，只拦 /vechain/** 路径
        registry.addInterceptor(vefutureInterceptor)
                .addPathPatterns("/vefuture/**");
    }
}