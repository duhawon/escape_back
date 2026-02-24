package com.untitled.escape.global.security;

import com.untitled.escape.global.exception.CustomException;
import com.untitled.escape.global.exception.code.AuthErrorCode;
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
            throw new CustomException(AuthErrorCode.UNAUTHENTICATED);
        }
        return extractUserId(authentication);
    }

    private static UUID extractUserId(Authentication authentication) {
        try {
            return UUID.fromString(authentication.getName());
        } catch (IllegalArgumentException e) {
            throw new CustomException(AuthErrorCode.INVALID_AUTHENTICATION);
        }
    }
    public static UUID getCurrentUserIdOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;
        try {
            return extractUserId(authentication);
        } catch (Exception e) {
            return null;
        }
    }
}
