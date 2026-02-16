package com.untitled.escape.domain.like.service;

import com.untitled.escape.domain.like.Like;
import com.untitled.escape.domain.like.TargetType;
import com.untitled.escape.domain.like.dto.LikeResponse;
import com.untitled.escape.domain.like.repository.LikeRepository;
import com.untitled.escape.domain.like.repository.projection.TargetLikeCount;
import com.untitled.escape.domain.user.dto.UserSummary;
import com.untitled.escape.domain.user.service.UserService;
import com.untitled.escape.global.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final UserService userService;

    public LikeServiceImpl(LikeRepository likeRepository, UserService userService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
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
    public List<UserSummary> getLikeUsers(TargetType targetType, Long targetId) {
        List<UUID> userIds = likeRepository.findUserIdsByTarget(targetType, targetId);
        List<UserSummary> userSummaries = userService.getUserSummaries(userIds).values().stream().toList();
        return userSummaries;
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
