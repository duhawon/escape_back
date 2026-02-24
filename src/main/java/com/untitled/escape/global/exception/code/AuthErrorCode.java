package com.untitled.escape.global.exception.code;

import com.untitled.escape.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHENTICATED("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    INVALID_AUTHENTICATION("유효하지 않은 인증 정보입니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_AUTH_FAILED("리프레시 토큰 인증에 실패하였습니다.", HttpStatus.UNAUTHORIZED)
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
