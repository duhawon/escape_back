package com.untitled.escape.global.exception.code;

import com.untitled.escape.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    EMAIL_ALREADY_IN_USE("이미 사용중인 이메일입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    PROFILE_IMAGE_KEY_REQUIRED("profileImgKey가 필요합니다.", HttpStatus.BAD_REQUEST),
    PROFILE_IMAGE_KEY_NOT_ALLOWED("허용되지 않은 profileImgKey 입니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
