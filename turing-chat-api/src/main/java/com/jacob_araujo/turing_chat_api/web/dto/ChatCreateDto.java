package com.jacob_araujo.turing_chat_api.web.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatCreateDto {
    private String chatName;
    private Boolean chatType;
    private LocalDateTime expiresAt;
}
