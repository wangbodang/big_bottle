package com.vefuture.big_bottle.common.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangb
 * @date 2025/5/1
 * @description TODO: 类描述
 */
@Data
@Component
@ConfigurationProperties(prefix = "websocket")
public class WebSocketProps {
    private List<String> allowedOrigins;
}
