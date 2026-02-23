package com.untitled.escape.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class SignInResponseDto {
    private final UUID userId;
    private final String email;
    private final String name;
    private final String profileImgUrl;

    @Builder
    public SignInResponseDto(UUID userId, String email, String name, String profileImgUrl) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.profileImgUrl = profileImgUrl;
    }
}
