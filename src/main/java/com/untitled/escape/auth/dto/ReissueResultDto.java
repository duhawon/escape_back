package com.untitled.escape.auth.dto;

import com.untitled.escape.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class ReissueResultDto {
    private String accessToken;
    private String refreshToken;
    private final User user;

    @Builder
    public ReissueResultDto(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }
    public static ReissueResponseDto convertFromDto(ReissueResultDto dto) {
        return new ReissueResponseDto(dto.user.getId(), dto.user.getEmail(), dto.user.getName(), dto.user.getProfileUrl());
    }
}
