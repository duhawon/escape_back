package com.untitled.escape.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class SignUpResponse {
    private final UUID userId;
    // DESC : 기본생성자(final userId)만 존재하기 때문에(필드를 받는 생성자가 없음)직접 생성자에 builder를 추가함
    @Builder
    public SignUpResponse(UUID userId) {
        this.userId = userId;
    }
}
