package com.untitled.escape.domain.upload.dto;

public record PresignResponse(
        String uploadUrl, // PUT 할 presigned url
        String key,       // DB에 저장할 key
        String publicUrl  // 미리보기용(선택)
) {}