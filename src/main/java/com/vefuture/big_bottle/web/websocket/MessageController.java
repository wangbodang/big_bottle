package com.vefuture.big_bottle.web.websocket;

/**
 * @author wangb
 * @date 2025/3/26
 * @description 服务端消息推送类
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
//@RequestMapping("/websocket")
public class MessageController {

    // 客户端发送消息到 /app/send
    @MessageMapping("/send")
    @SendTo("/topic/messages")  // 广播到这个 topic
    public String sendMessage(String message) {
        log.info("---> 前端发送过来的消息:{}", message);
        return "服务器返回：" + message;
    }

}
