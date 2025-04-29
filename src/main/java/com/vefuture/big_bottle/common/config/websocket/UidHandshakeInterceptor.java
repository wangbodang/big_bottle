package com.vefuture.big_bottle.common.config.websocket;

/**
 * @author wangb
 * @date 2025/4/29
 * @description TODO: 类描述
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
public class UidHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        // 1. 解析查询串里的 uid
        String uid = UriComponentsBuilder.fromUri(request.getURI())
                .build()
                .getQueryParams()
                .getFirst("uid");
        log.info("-->>>>>>>>>>>>>>>>>>>>>>>>>>>>>websocket 握手时找到用户ID:{}", uid);
        // 2. 写入 attributes 供后续 Handler 使用
        attributes.put("uid", uid == null ? "UNKNOWN" : uid);
        return true;                // true 表示握手通过；返回 false 会被拒绝
    }

    @Override
    public void afterHandshake(ServerHttpRequest req,
                               ServerHttpResponse res,
                               WebSocketHandler wsHandler,
                               Exception exception) {

        // 这里通常不用写逻辑
    }
}

