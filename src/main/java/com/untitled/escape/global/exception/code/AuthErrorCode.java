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
    REFRESH_TOKEN_AUTH_FAILED("리프레시 토큰 인증에 실패하였습니다.", HttpStatus.UNAUTHORIZED),
    OAUTH2_USER_INFO_NOT_FOUND("OAuth2 사용자 정보를 가져올 수 없습니다.", HttpStatus.UNAUTHORIZED),
    OAUTH2_PROVIDER_NOT_SUPPORTED("지원하지 않는 OAuth2 제공자입니다.", HttpStatus.BAD_REQUEST),
    OAUTH2_PROVIDER_USER_ID_MISSING("OAuth2 제공자 사용자 식별자가 없습니다.", HttpStatus.BAD_REQUEST),
    OAUTH2_EMAIL_NOT_PROVIDED("OAuth2 이메일 정보가 없습니다.", HttpStatus.BAD_REQUEST),
    OAUTH2_EMAIL_ALREADY_REGISTERED("이미 같은 이메일로 가입된 계정이 있습니다.", HttpStatus.CONFLICT),
    OAUTH2_LOGIN_CODE_INVALID_OR_EXPIRED("유효하지 않거나 만료된 OAuth 로그인 코드입니다.", HttpStatus.UNAUTHORIZED)
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
