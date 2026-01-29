package com.untitled.escape.domain.user.service;

import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.dto.SignUpRequestDto;
import com.untitled.escape.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User signUp(SignUpRequestDto signUpRequestDto) {
        // 1. 기존에 사용중인 이메일인지 확인
        userRepository.findByEmail(signUpRequestDto.getEmail())
                .ifPresent(user -> {
                    // TODO : CustomException으로 변경
                    throw new RuntimeException("이미 사용중인 이메일입니다.");
                });
        // 2. user 객체 생성 (passwordEncoder salt 사용)
        User user = User.builder()
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .name(signUpRequestDto.getName())
                .build();
        // 3. 저장
        userRepository.save(user);
        // 4. user반환
        return user;
    }
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    // TODO : CustomException으로 변경
                    throw new RuntimeException("사용자를 찾을 수 없습니다.");
                });
    }

    @Override
    public User getById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    // TODO : CustomException으로 변경
                    throw new RuntimeException("사용자를 찾을 수 없습니다.");
                });
    }
}
