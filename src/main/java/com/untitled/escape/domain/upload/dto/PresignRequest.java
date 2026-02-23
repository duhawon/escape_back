package com.untitled.escape.domain.upload.dto;

public record PresignRequest(
        String contentType,       // "image/png" 같은 값
        String originalFileName    // 확장자 추정용 (선택)
) {}
