package com.untitled.escape.domain.user.service;

import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.dto.SignUpRequest;
import com.untitled.escape.domain.user.dto.UserSummary;
import com.untitled.escape.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User signUp(SignUpRequest signUpRequest) {
        // 1. 기존에 사용중인 이메일인지 확인
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent(user -> {
                    // TODO : CustomException으로 변경
                    throw new RuntimeException("이미 사용중인 이메일입니다.");
                });
        // 2. user 객체 생성 (passwordEncoder salt 사용)
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .name(signUpRequest.getName())
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
    @Override
    public UserSummary getUserSummary(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
        return UserSummary.builder()
                .userId(user.getId())
                .name(user.getName())
                .profileImgUrl(user.getProfileUrl())
                .build();
    }
    @Override
    public Map<UUID, UserSummary> getUserSummaries(List<UUID> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        return users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> UserSummary.builder()
                                .userId(user.getId())
                                .name(user.getName())
                                .profileImgUrl(user.getProfileUrl())
                                .build()
                ));
    }

    @Override
    public User getReference(UUID userID) {
        return userRepository.getReferenceById(userID);
    }
}
