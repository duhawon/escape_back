package com.untitled.escape.global.exception.code;

import com.untitled.escape.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
    REVIEW_NOT_FOUND("존재하지 않는 리뷰입니다.", HttpStatus.NOT_FOUND),
    REVIEW_NOT_OWNER("본인 리뷰만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
    REVIEW_ALREADY_EXISTS("이미 이 방에 리뷰를 작성했습니다.", HttpStatus.CONFLICT),
    ;
    private final String message;
    private final HttpStatus httpStatus;
}
