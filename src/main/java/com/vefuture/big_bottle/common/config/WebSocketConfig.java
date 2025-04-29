package com.vefuture.big_bottle.common.config;

import com.vefuture.big_bottle.common.config.websocket.UidHandshakeInterceptor;
import com.vefuture.big_bottle.web.websocket.EchoHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import javax.annotation.PostConstruct;

@Configuration
//@EnableWebSocketMessageBroker
@EnableWebSocket
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
public class WebSocketConfig implements WebSocketConfigurer  {

    /*@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // ⚠️ 就是 /ws，别加 /bigbottle
                .setAllowedOrigins("*");
                //.withSockJS(); //注掉该行使用原生websocket
    }*/
    @PostConstruct
    public void init() { System.out.println(">>>> WebSocketConfig loaded"); }

    private final EchoHandler echoHandler;
    private final UidHandshakeInterceptor uidInterceptor;

    public WebSocketConfig(EchoHandler echoHandler,
                           UidHandshakeInterceptor uidInterceptor) {
        this.echoHandler   = echoHandler;
        this.uidInterceptor = uidInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoHandler, "/ws")
                .addInterceptors(uidInterceptor) // 注册拦截器
                .setAllowedOrigins("*");
    }

    /*
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    */
}
