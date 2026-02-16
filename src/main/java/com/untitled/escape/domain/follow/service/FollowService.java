package com.untitled.escape.domain.follow.service;

import com.untitled.escape.domain.user.dto.UserSummary;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface FollowService {
    List<UserSummary> getFollowings(UUID userId);

    List<UserSummary> getFollowers(UUID userId);

    void follow(UUID targetUserId);

    void unfollow(UUID targetUserId);
}
