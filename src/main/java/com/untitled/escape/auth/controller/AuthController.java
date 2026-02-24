package com.untitled.escape.auth.controller;

import com.untitled.escape.auth.constant.AuthConstants;
import com.untitled.escape.auth.dto.ReissueResultDto;
import com.untitled.escape.auth.dto.SignInRequestDto;
import com.untitled.escape.auth.dto.SignInResultDto;
import com.untitled.escape.auth.mapper.AuthResponseMapper;
import com.untitled.escape.auth.service.AuthService;
import com.untitled.escape.auth.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;
    private final AuthService authService;
    private final AuthResponseMapper authResponseMapper;

    public AuthController(AuthService authService, AuthResponseMapper authResponseMapper) {
        this.authService = authService;
        this.authResponseMapper = authResponseMapper;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto signInRequestDto) {
        SignInResultDto signInResultDto = authService.signIn(signInRequestDto);
        ResponseCookie refreshTokenCookie = CookieUtils.createRefreshTokenCookie(signInResultDto.getRefreshToken(),refreshTokenExpirationMs);
        var responseBody = authResponseMapper.toSignInResponse(signInResultDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + signInResultDto.getAccessToken())
                .body(responseBody);
    }

    @PostMapping("/signOut")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        String refreshToken = CookieUtils.getCookieValue(request, AuthConstants.REFRESH_TOKEN);
        if (refreshToken != null) {
            authService.signOut(refreshToken);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, CookieUtils.deleteRefreshTokenCookie().toString())
                .build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue(value = AuthConstants.REFRESH_TOKEN, required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            // TODO : CustomException으로 변경 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ReissueResultDto reissueResultDto = authService.reissue(refreshToken);
        ResponseCookie refreshTokenCookie = CookieUtils.createRefreshTokenCookie(reissueResultDto.getRefreshToken(), refreshTokenExpirationMs);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + reissueResultDto.getAccessToken())
                .body(authResponseMapper.toReissueResponse(reissueResultDto));
    }
}
