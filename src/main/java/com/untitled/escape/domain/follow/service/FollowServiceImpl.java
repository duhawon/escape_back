package com.untitled.escape.domain.follow.service;

import com.untitled.escape.domain.follow.Follow;
import com.untitled.escape.domain.follow.dto.FollowCountResponse;
import com.untitled.escape.domain.follow.repository.FollowRepository;
import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.dto.UserSummary;
import com.untitled.escape.domain.user.service.UserService;
import com.untitled.escape.global.exception.CustomException;
import com.untitled.escape.global.exception.code.FollowErrorCode;
import com.untitled.escape.global.security.SecurityUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
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
    public Slice<UserSummary> getFollowings(UUID userId, Pageable pageable) {
        // DESC : 내가 팔로우 한 사람들 follower가 나인사람
        Slice<UUID> idSlice = followRepository.findFollowingIds(userId, pageable);
        List<UUID> ids = idSlice.getContent();
        if (ids.isEmpty()) {
            return new SliceImpl<>(List.of(), pageable, false);
        }
        Map<UUID, UserSummary> userMap = userService.getUserSummaries(ids);
        return idSlice.map(userMap::get);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<UserSummary> getFollowers(UUID userId, Pageable pageable) {
        // DESC : 나를 팔로우 한 사람들 followee가 나인사람
        Slice<UUID> idSlice = followRepository.findFollowerIds(userId, pageable);
        List<UUID> ids = idSlice.getContent();

        if (ids.isEmpty()) {
            return new SliceImpl<>(List.of(), pageable, false);
        }

        Map<UUID, UserSummary> userMap = userService.getUserSummaries(ids);
        return idSlice.map(userMap::get);
    }

    @Override
    public void follow(UUID targetUserId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (userId.equals(targetUserId)) {
            throw new CustomException(FollowErrorCode.SELF_FOLLOW_NOT_ALLOWED);
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

    @Override
    @Transactional(readOnly = true)
    public FollowCountResponse getFollowCounts(UUID userId) {
        long followers = followRepository.countByFollowee_Id(userId);
        long following = followRepository.countByFollower_Id(userId);
        return new FollowCountResponse(followers, following);
    }
}
