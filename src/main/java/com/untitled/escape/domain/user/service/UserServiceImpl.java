package com.untitled.escape.domain.user.service;

import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.dto.SignUpRequest;
import com.untitled.escape.domain.user.dto.UserSummary;
import com.untitled.escape.domain.user.repository.UserRepository;
import com.untitled.escape.global.exception.CustomException;
import com.untitled.escape.global.exception.code.UserErrorCode;
import com.untitled.escape.global.s3.S3UrlResolver;
import com.untitled.escape.global.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UrlResolver s3UrlResolver;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, S3UrlResolver s3UrlResolver) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3UrlResolver = s3UrlResolver;
    }

    @Override
    @Transactional
    public User signUp(SignUpRequest signUpRequest) {
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(UserErrorCode.EMAIL_ALREADY_IN_USE);
                });
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .name(signUpRequest.getName())
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new CustomException(UserErrorCode.USER_NOT_FOUND);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new CustomException(UserErrorCode.USER_NOT_FOUND);
                });
    }
    @Override
    @Transactional(readOnly = true)
    public UserSummary getUserSummary(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return UserSummary.builder()
                .userId(user.getId())
                .name(user.getName())
                .profileImgUrl(s3UrlResolver.toPublicUrl(user.getProfileImgKey()))
                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public Map<UUID, UserSummary> getUserSummaries(List<UUID> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        return users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> UserSummary.builder()
                                .userId(user.getId())
                                .name(user.getName())
                                .profileImgUrl(s3UrlResolver.toPublicUrl(user.getProfileImgKey()))
                                .build()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public User getReference(UUID userID) {
        return userRepository.getReferenceById(userID);
    }

    @Override
    @Transactional
    public void updateMyProfileImage(String profileImgKey) {
        validateProfileKey(profileImgKey);
        UUID userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        user.updateProfileImgKey(profileImgKey);
    }
    private void validateProfileKey(String key) {
        if (key == null || key.isBlank()) {
            throw new CustomException(UserErrorCode.PROFILE_IMAGE_KEY_REQUIRED);
        }
        if (!key.startsWith("users/profiles/")) {
            throw new CustomException(UserErrorCode.PROFILE_IMAGE_KEY_NOT_ALLOWED);
        }
    }
}
