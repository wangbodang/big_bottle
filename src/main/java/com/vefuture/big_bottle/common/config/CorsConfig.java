package com.vefuture.big_bottle.common.config;

import com.vefuture.big_bottle.common.config.prop.WebSocketProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 允许跨域调用
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class CorsConfig implements WebMvcConfigurer{

    private final WebSocketProps props;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有 HTTP 请求都支持跨域
                //.allowedOrigins(props.getAllowedOrigins().toArray(new String[0]))
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                //.allowCredentials(true)
                .maxAge(3600);
    }
}
