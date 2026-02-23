package com.untitled.escape.domain.like.service;

import com.untitled.escape.domain.follow.repository.FollowRepository;
import com.untitled.escape.domain.like.Like;
import com.untitled.escape.domain.like.TargetType;
import com.untitled.escape.domain.like.dto.LikeResponse;
import com.untitled.escape.domain.like.dto.LikerResponse;
import com.untitled.escape.domain.like.repository.LikeRepository;
import com.untitled.escape.domain.like.repository.projection.TargetLikeCount;
import com.untitled.escape.domain.user.dto.UserSummary;
import com.untitled.escape.domain.user.service.UserService;
import com.untitled.escape.global.security.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final FollowRepository followRepository;

    public LikeServiceImpl(LikeRepository likeRepository, UserService userService, FollowRepository followRepository) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.followRepository = followRepository;
    }

    @Override
    @Transactional
    public LikeResponse like(TargetType targetType, Long targetId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if(!likeRepository.existsByUserIdAndTargetTypeAndTargetId(
                userId,  targetType, targetId)) {
            Like like = Like.builder()
                    .userId(userId)
                    .targetType(targetType)
                    .targetId(targetId)
                    .build();
            likeRepository.save(like);
        }
        long count = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
        return new LikeResponse(count, true);
    }
    @Override
    @Transactional
    public LikeResponse unlike(TargetType targetType, Long targetId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        likeRepository.deleteByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        long count = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
        return new LikeResponse(count, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<LikerResponse> getLikeUsers(TargetType targetType, Long targetId, Pageable pageable) {
        Slice<UUID> userIdSlice = likeRepository.findUserIdsByTarget(targetType, targetId, pageable);
        List<UUID> userIds = userIdSlice.getContent();
        if (userIds.isEmpty()) {
            return new SliceImpl<>(List.of(), pageable, false);
        }
        Map<UUID, UserSummary> userMap = userService.getUserSummaries(userIds);
        UUID userId = SecurityUtils.getCurrentUserId();
        List<UUID> followingIds = followRepository.findFollowingIdsIn(userId, userIds);
        Set<UUID> followingSet = new HashSet<>(followingIds);
        return userIdSlice.map(uid ->
                LikerResponse.of(userMap.get(uid), followingSet.contains(uid)));
    }

    @Override
    @Transactional(readOnly = true)
    public long getLikeCount(Long targetId, TargetType targetType) {
        return likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Long> getLikeCountMap(List<Long> targetIds, TargetType targetType) {
        List<TargetLikeCount> targetLikeCounts = likeRepository.countGroupedByTargetIdAndTargetType(
                targetIds,
                targetType);

        return targetLikeCounts.stream()
                .collect(Collectors.toMap(
                        TargetLikeCount::getTargetId,
                        TargetLikeCount::getCount));
    }
}
