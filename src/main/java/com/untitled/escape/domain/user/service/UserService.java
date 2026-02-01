package com.untitled.escape.domain.user.service;

import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.dto.SignUpRequest;

import java.util.UUID;

public interface UserService {
    User signUp(SignUpRequest signUpRequest);
    User getByEmail(String email);
    User getById(UUID userId);
}
