package com.vefuture.big_bottle.web.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangb
 * @date 2025/4/29
 * @description TODO: 类描述
 */
@Component
public class WsSessionManager {

    /** userId -> WebSocketSession */
    private final ConcurrentHashMap<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    /* ------------ 内部维护 ------------- */
    public void addSession(String userId, WebSocketSession s)   { SESSIONS.put(userId, s); }
    public void removeSession(String userId)                    { SESSIONS.remove(userId); }

    /* ------------ 业务调用 ------------- */
    /** 推送给所有在线客户端 */
    public void sendToAll(String text) {
        TextMessage msg = new TextMessage(text);
        SESSIONS.values().forEach(s -> trySend(s, msg));
    }

    /** 推送给指定用户 */
    public void sendToUser(String userId, String text) {
        WebSocketSession s = SESSIONS.get(userId);
        if (s != null) trySend(s, new TextMessage(text));
    }

    private void trySend(WebSocketSession s, TextMessage msg) {
        if (s.isOpen()) {
            try { s.sendMessage(msg); }
            catch (IOException e) { e.printStackTrace(); }
        }
    }
}

