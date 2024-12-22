package com.jacob_araujo.turing_chat_api.service;

import com.jacob_araujo.turing_chat_api.entity.Chat;
import com.jacob_araujo.turing_chat_api.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    public Chat createGroup(String groupName, Boolean groupType, LocalDateTime expiresAt) {
        Chat group = new Chat();
        group.setGroupName(groupName);
        group.setChatType(groupType ? Chat.ChatType.PUBLIC : Chat.ChatType.PRIVATE);
        group.setCreatedAt(LocalDateTime.now());
        group.setExpiresAt(expiresAt);
        System.out.println(group);
        return chatRepository.save(group);
    }

    public void deleteExpiredGroups() {
        List<Chat> expiredGroups = chatRepository.findByExpiresAtAfter(LocalDateTime.now());
        chatRepository.deleteAll(expiredGroups);
    }

    public List<Chat> getPublicGroups() {
        return chatRepository.findByChatType(Chat.ChatType.PUBLIC);
    }
}

