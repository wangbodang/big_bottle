package com.vefuture.big_bottle.web.websocket;

/**
 * @author wangb
 * @date 2025/3/26
 * @description 服务端消息推送类
 */

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
/**
 * 前端 send("/app/echo", body) -> 这里收到
 * 服务端广播到 /topic/echo
 */
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate template;

    /** 客户端发到 /app/echo，会自动路由到这里 */
    @MessageMapping("/echo")
    @SendTo("/topic/echo")               // 直接广播
    public String echo(String msg) {
        log.info("-----> 在后台{}, 收到消息:{}", this.getClass().getName(), msg);
        return "服务器返回：" + msg;
    }

    /** 后端业务推送：在任何 Service 注入 template */
    public void pushToUser(String uid, Object payload) {
        // 前端订阅 /user/queue/notice
        template.convertAndSendToUser(uid, "/queue/notice", payload);
    }
}