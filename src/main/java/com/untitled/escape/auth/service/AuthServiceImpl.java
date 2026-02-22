package com.untitled.escape.auth.service;

import com.untitled.escape.auth.dto.ReissueResultDto;
import com.untitled.escape.auth.dto.SignInRequestDto;
import com.untitled.escape.auth.dto.SignInResultDto;
import com.untitled.escape.auth.jwt.JwtTokenProvider;
import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public SignInResultDto signIn(SignInRequestDto signInRequestDto) {
        // 1. 사용자 인증
        User validatedUser = getValidatedUser(signInRequestDto);
        // 2. 엑세스 토큰발급
        String accessToken = jwtTokenProvider.createAccessToken(validatedUser);
        // 3. 리프레시 토큰발급
        String refreshToken = jwtTokenProvider.createRefreshToken(validatedUser);
        // 4. 레디스에 리프레시 토큰 저장
        refreshTokenService.save(validatedUser.getId(), refreshToken);
        // 5. response 리턴
        return new SignInResultDto().builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .user(validatedUser)
                .build();
    }

    @Override
    public void signOut(String refreshToken) {
        // 1. redis에서 refreshToken 삭제
        refreshTokenService.delete(jwtTokenProvider.getTokenId(refreshToken));
        // 2. 실패시 에러 redis나 서버 내부 오류일경우에만.. (선택)
    }

    @Override
    public ReissueResultDto reissue(String refreshToken) {
        // 3. refreshToken redis 검증
        if(!refreshTokenService.exists(jwtTokenProvider.getTokenId(refreshToken), refreshToken)) {
            // TODO : CustomException으로 변경
            throw new RuntimeException("리프레시 토큰 인증에 실패하였습니다.");
        };
        // 4. RefreshToken에서 사용자 정보 추출
        UUID userId = jwtTokenProvider.getTokenId(refreshToken);
        User user = userService.getById(userId);
        // 5. 새로운 accessToken생성
        String accessToken = jwtTokenProvider.createAccessToken(user);
        // 6. refreshToken Rotation (기존 rt redis에서 삭제 -> 새로운 rt생성 -> redis 재 저장)
        refreshTokenService.delete(jwtTokenProvider.getTokenId(refreshToken));
        String reissueRefreshToken = jwtTokenProvider.createRefreshToken(user);
        refreshTokenService.save(user.getId(), reissueRefreshToken);
        // 7. response 리턴
        return new ReissueResultDto().builder()
                .accessToken(accessToken)
                .refreshToken(reissueRefreshToken)
                .user(user)
                .build();
    }

    private User getValidatedUser(SignInRequestDto signInRequestDto) {
        User user = userService.getByEmail(signInRequestDto.getEmail());
        // TODO : CustomException으로 변경
        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }
        return user;
    }
}
