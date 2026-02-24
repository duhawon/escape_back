package com.untitled.escape.global.exception;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import com.untitled.escape.global.dto.response.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        LOGGER.warn("[CustomException] {} - {}", errorCode.getClass().getSimpleName(), errorCode.getMessage(), e);

        String code = extractCodeName(errorCode);
        ErrorResponse body = new ErrorResponse(code, errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        LOGGER.error("[UnhandledException]", e);

        ErrorResponse body = new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다.");
        return ResponseEntity.status(500).body(body);
    }

    private String extractCodeName(ErrorCode errorCode) {
        if (errorCode instanceof Enum<?> enumCode) {
            return enumCode.name();
        }
        return errorCode.getClass().getSimpleName();
    }
}
