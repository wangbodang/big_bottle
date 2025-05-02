package com.vefuture.big_bottle.common.config.websocket;

import com.vefuture.big_bottle.common.config.prop.WebSocketProps;
import com.vefuture.big_bottle.common.config.websocket.UidHandshakeInterceptor;
import com.vefuture.big_bottle.web.websocket.EchoHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketProps props;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(props.getAllowedOrigins().toArray(new String[0]))  // ✅ 兼容 allowCredentials
                .withSockJS(); // ← ⚠️ SockJS 必须启用
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }
}
