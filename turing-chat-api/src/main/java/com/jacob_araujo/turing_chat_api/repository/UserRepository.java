package com.jacob_araujo.turing_chat_api.repository;

import com.jacob_araujo.turing_chat_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByTokenEmailVerification(String token);

    Optional<User> findByResetToken(String token);
}
