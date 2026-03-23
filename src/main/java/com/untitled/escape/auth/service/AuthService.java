package com.untitled.escape.auth.service;

import com.untitled.escape.auth.dto.ReissueResultDto;
import com.untitled.escape.auth.dto.SignInRequestDto;
import com.untitled.escape.auth.dto.SignInResultDto;

public interface AuthService {
    SignInResultDto signIn(SignInRequestDto signInRequestDto);
    void signOut(String RefreshToken);
    ReissueResultDto reissue(String RefreshToken);
    SignInResultDto exchangeOAuthCode(String code);
}
