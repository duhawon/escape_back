package com.untitled.escape.auth.utils;
import com.untitled.escape.auth.constant.AuthConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public final class CookieUtils {

    private CookieUtils() {}

    public static ResponseCookie createRefreshTokenCookie(String token, long maxAge) {
        return ResponseCookie.from(AuthConstants.REFRESH_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    public static ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(AuthConstants.REFRESH_TOKEN,"")
                .path("/")
                .maxAge(0)
                .build();
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
