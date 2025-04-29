package com.vefuture.big_bottle.web.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * @author wangb
 * @date 2025/4/29
 * @description TODO: 类描述
 */
@Slf4j
@Component
public class EchoHandler extends TextWebSocketHandler {

    private final WsSessionManager sessionManager;
    public EchoHandler(WsSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override                              // 握手成功后立刻触发
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        // 取出在拦截器里存的 uid
        String uid = (String) session.getAttributes().get("uid");
        sessionManager.addSession(uid, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {
        log.info("------->>>>>>> websocket调用了:{}", message.getPayload());
        // 原样回显
        session.sendMessage(
                new TextMessage("服务器返回：" + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) {
        String uid = (String) session.getAttributes().get("uid");
        sessionManager.removeSession(uid);
    }
}