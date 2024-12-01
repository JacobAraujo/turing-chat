package com.jacob_araujo.turing_chat_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 25)
    private Role role = Role.ROLE_CLIENT;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_verification_status", nullable = false, length = 25)
    private EmailVerificationStatus emailVerificationStatus = EmailVerificationStatus.PENDING;

    @Column(name = "token_email_verification", length = 255)
    private String tokenEmailVerification;

    @Column(name = "reset_token", length = 255)
    private String resetToken;

    @Column(name = "token_expiration")
    private LocalDateTime tokenExpiration;

    @Column(name = "available_message_limit")
    private Long availableMessageLimit = 5L;

    @Column(name = "renovation_limit_date")
    private LocalDateTime renovationLimitDate;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "modification_date")
    private LocalDateTime modificationDate;
    @Column(name = "created_by")
    private String createBy;
    @Column(name = "modified_by")
    private String modifiedBy;

    public enum Role {
        ROLE_ADMIN, ROLE_CLIENT
    }

    public enum EmailVerificationStatus {
        PENDING, VERIFIED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return username;
    }
}

