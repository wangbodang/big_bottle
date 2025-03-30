package com.vefuture.big_bottle.common.config;

// WebConfig.java
import com.vefuture.big_bottle.common.interceptor.JwtInterceptor;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
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
    }
}