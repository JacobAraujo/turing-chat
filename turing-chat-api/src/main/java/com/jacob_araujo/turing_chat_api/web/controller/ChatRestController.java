package com.jacob_araujo.turing_chat_api.web.controller;

import com.jacob_araujo.turing_chat_api.entity.Chat;
import com.jacob_araujo.turing_chat_api.service.ChatService;
import com.jacob_araujo.turing_chat_api.web.dto.ChatCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/groups")
    public Chat createGroup(@RequestBody ChatCreateDto dto) {
        return chatService.createGroup(dto.getChatName(), dto.getChatType(), dto.getExpiresAt());
    }

    @GetMapping("/public-groups")
    public List<Chat> getPublicGroups() {
        return chatService.getPublicGroups();
    }
}

