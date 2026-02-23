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
        // DESC : 1. accessToken, refreshToken 생성
        SignInResultDto signInResultDto = authService.signIn(signInRequestDto);
        // DESC : 2. refreshToken 쿠키 생성
        ResponseCookie refreshTokenCookie = CookieUtils.createRefreshTokenCookie(signInResultDto.getRefreshToken(),refreshTokenExpirationMs);
        // DESC : 3. 사용자 정보 가져오기
        var responseBody = authResponseMapper.toSignInResponse(signInResultDto);
        // DESC : 4. response 리턴
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + signInResultDto.getAccessToken())
                .body(responseBody);
    }

    @PostMapping("/signOut")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        // DESC : 1. 쿠키에서 refreshToken 추출
        String refreshToken = CookieUtils.getCookieValue(request, AuthConstants.REFRESH_TOKEN);
        // DESC : 2. refreshToken signOut 처리
        if (refreshToken != null) {
            authService.signOut(refreshToken);
        }
        // DESC : 3. 쿠키 삭제, response 리턴
        // TODO : client(Frontend)에서 저장된 accessToken 삭제
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, CookieUtils.deleteRefreshTokenCookie().toString())
                .build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue(value = AuthConstants.REFRESH_TOKEN, required = false) String refreshToken) {
        // DESC : 1. refreshToken 유효성 검사
        if (refreshToken == null || refreshToken.isBlank()) {
            // TODO : CustomException으로 변경 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // DESC : 2. refreshToken rt, accessToken 재발급
        ReissueResultDto reissueResultDto = authService.reissue(refreshToken);
        // DESC : 3. refreshToken 쿠키 생성
        ResponseCookie refreshTokenCookie = CookieUtils.createRefreshTokenCookie(reissueResultDto.getRefreshToken(), refreshTokenExpirationMs);
        // DESC : 4. response 리턴
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + reissueResultDto.getAccessToken())
                .body(authResponseMapper.toReissueResponse(reissueResultDto));
    }
}
