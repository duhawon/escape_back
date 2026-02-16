package com.untitled.escape.domain.follow.service;

import com.untitled.escape.domain.follow.Follow;
import com.untitled.escape.domain.follow.repository.FollowRepository;
import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.dto.UserSummary;
import com.untitled.escape.domain.user.service.UserService;
import com.untitled.escape.global.security.SecurityUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    public FollowServiceImpl(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummary> getFollowings(UUID userId) {
        // follower가 나인사람
        List<Follow> follows = followRepository.findAllByFollower_Id(userId);
        List<UUID> userIds = follows.stream()
                .map(follow ->  follow.getFollowee().getId())
                .toList();
        return userService.getUserSummaries(userIds).values().stream().toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummary> getFollowers(UUID userId) {
        // followee가 나인사람
        List<Follow> follows = followRepository.findAllByFollowee_Id(userId);
        List<UUID> userIds = follows.stream()
                .map(follow -> follow.getFollower().getId())
                .toList();
        return userService.getUserSummaries(userIds).values().stream().toList();
    }

    @Override
    public void follow(UUID targetUserId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (userId.equals(targetUserId)) {
            // TODO : CustomException으로 변경
            throw new RuntimeException("자기 자신을 팔로우 할 수 없습니다.");
        }

        User follower = userService.getReference(userId);
        User followee = userService.getReference(targetUserId);
        Follow follow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();
        try {
            followRepository.save(follow);
        } catch (DataIntegrityViolationException e) {}
    }

    @Override
    public void unfollow(UUID targetUserId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        followRepository.deleteByFollower_IdAndFollowee_Id(userId, targetUserId);
    }
}
