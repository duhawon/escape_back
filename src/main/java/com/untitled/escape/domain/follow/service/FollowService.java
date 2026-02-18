package com.untitled.escape.domain.follow.service;

import com.untitled.escape.domain.user.dto.UserSummary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.UUID;

public interface FollowService {
    Slice<UserSummary> getFollowings(UUID userId, Pageable pageable);

    Slice<UserSummary> getFollowers(UUID userId, Pageable pageable);

    void follow(UUID targetUserId);

    void unfollow(UUID targetUserId);
}
