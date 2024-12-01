package com.jacob_araujo.turing_chat_api.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDto {

    private Long id;
    private String username;
    private String role;
    private String emailVerificationStatus;
}