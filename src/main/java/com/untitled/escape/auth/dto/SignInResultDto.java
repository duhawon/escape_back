package com.untitled.escape.auth.dto;

import com.untitled.escape.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class SignInResultDto {
    private final String accessToken;
    private final String refreshToken;
    private final User user;

    @Builder
    public SignInResultDto(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }
}
