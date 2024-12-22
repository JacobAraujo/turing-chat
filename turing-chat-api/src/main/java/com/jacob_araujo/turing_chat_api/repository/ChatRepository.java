package com.jacob_araujo.turing_chat_api.repository;

import com.jacob_araujo.turing_chat_api.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByExpiresAtAfter(LocalDateTime now);

    List<Chat> findByChatType(Chat.ChatType chatType);
}

