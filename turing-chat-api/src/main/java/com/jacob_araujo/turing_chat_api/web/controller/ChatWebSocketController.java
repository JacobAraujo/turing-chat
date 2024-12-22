package com.jacob_araujo.turing_chat_api.web.controller;

import com.jacob_araujo.turing_chat_api.dto.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @MessageMapping("/sendMessage/{groupId}")
    @SendTo("/topic/group/{groupId}")
    public Message sendMessage(@DestinationVariable Long groupId, Message message) {
        message.setTimestamp(java.time.LocalDateTime.now());
        return message;
    }
}

