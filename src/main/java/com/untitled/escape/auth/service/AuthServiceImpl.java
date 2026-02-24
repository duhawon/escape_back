package com.untitled.escape.auth.service;

import com.untitled.escape.auth.dto.ReissueResultDto;
import com.untitled.escape.auth.dto.SignInRequestDto;
import com.untitled.escape.auth.dto.SignInResultDto;
import com.untitled.escape.auth.jwt.JwtTokenProvider;
import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.service.UserService;
import com.untitled.escape.global.exception.CustomException;
import com.untitled.escape.global.exception.code.AuthErrorCode;
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
        User validatedUser = getValidatedUser(signInRequestDto);
        String accessToken = jwtTokenProvider.createAccessToken(validatedUser);
        String refreshToken = jwtTokenProvider.createRefreshToken(validatedUser);
        refreshTokenService.save(validatedUser.getId(), refreshToken);
        return new SignInResultDto().builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .user(validatedUser)
                .build();
    }

    @Override
    public void signOut(String refreshToken) {
        refreshTokenService.delete(jwtTokenProvider.getTokenId(refreshToken));
    }

    @Override
    public ReissueResultDto reissue(String refreshToken) {
        if(!refreshTokenService.exists(jwtTokenProvider.getTokenId(refreshToken), refreshToken)) {
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_AUTH_FAILED);
        };
        UUID userId = jwtTokenProvider.getTokenId(refreshToken);
        User user = userService.getById(userId);
        String accessToken = jwtTokenProvider.createAccessToken(user);
        refreshTokenService.delete(jwtTokenProvider.getTokenId(refreshToken));
        String reissueRefreshToken = jwtTokenProvider.createRefreshToken(user);
        refreshTokenService.save(user.getId(), reissueRefreshToken);
        return ReissueResultDto.builder()
                .accessToken(accessToken)
                .refreshToken(reissueRefreshToken)
                .user(user)
                .build();
    }

    private User getValidatedUser(SignInRequestDto signInRequestDto) {
        User user = userService.getByEmail(signInRequestDto.getEmail());
        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(AuthErrorCode.INVALID_PASSWORD);
        }
        return user;
    }
}
