package com.untitled.escape.domain.user.service;

import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.dto.SignUpRequest;
import com.untitled.escape.domain.user.dto.UserSummary;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {
    User signUp(SignUpRequest signUpRequest);
    User getByEmail(String email);
    User getById(UUID userId);
    UserSummary getUserSummary(UUID userId);
    Map<UUID, UserSummary> getUserSummaries(List<UUID> userIds);
    User getReference(UUID userID);

    void updateMyProfileImage(String profileImgKey);
}
