package com.vefuture.big_bottle.common.config.websocket;

/**
 * @author wangb
 * @date 2025/5/1
 * @description TODO: 类描述
 */
import com.vefuture.big_bottle.common.config.prop.WebSocketProps;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class GlobalCorsConfig {

    private final WebSocketProps props;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 应用到所有路径
                registry.addMapping("/**")
                        // !!! 允许你的前端来源。开发时用 "*" 也可以，但生产环境务必精确指定
                        .allowedOrigins(props.getAllowedOrigins().toArray(new String[0]))
                        // 允许的 HTTP 方法
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // 允许所有请求头
                        .allowedHeaders("*")
                        // 是否允许发送 Cookie 等凭证信息
                        .allowCredentials(true);
            }
        };
    }
}