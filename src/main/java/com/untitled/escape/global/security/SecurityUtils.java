package com.untitled.escape.global.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

// TODO : 다시 확인 후 점검 및 주석 필요
public class SecurityUtils {
    private SecurityUtils() {}

    public static UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            // TODO : CustomException으로 변경
            throw new RuntimeException("인증된 사용자 정보가 없습니다.");
        }
        return extractUserId(authentication);
    }

    private static UUID extractUserId(Authentication authentication) {
        try {
            return UUID.fromString(authentication.getName());
        } catch (IllegalArgumentException e) {
            // TODO : CustomException으로 변경?
            throw new IllegalStateException("잘못된 인증 사용자 ID 형식입니다.", e);
        }
    }

}
