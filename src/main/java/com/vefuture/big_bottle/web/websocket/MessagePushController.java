package com.vefuture.big_bottle.web.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangb
 * @date 2025/3/26
 * @description TODO: 类描述
 */
//@RestController
//@RequestMapping("/websocket")
public class MessagePushController {

    private final SimpMessagingTemplate messagingTemplate;

    public MessagePushController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/sendToAll")
    public void sendToAll(@RequestParam String msg) {
        messagingTemplate.convertAndSend("/topic/messages", msg);
    }
}
