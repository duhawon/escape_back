package com.untitled.escape.global.exception.code;

import com.untitled.escape.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum FollowErrorCode implements ErrorCode {
    SELF_FOLLOW_NOT_ALLOWED("자기 자신을 팔로우 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    TARGET_USER_NOT_FOUND("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    FOLLOW_ALREADY_EXISTS("이미 팔로우 중입니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;
}
