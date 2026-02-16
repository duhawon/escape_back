package com.untitled.escape.domain.like.service;

import com.untitled.escape.domain.like.TargetType;
import com.untitled.escape.domain.like.dto.LikeResponse;
import com.untitled.escape.domain.user.dto.UserSummary;

import java.util.List;
import java.util.Map;

public interface LikeService {
    long getLikeCount(Long targetId, TargetType targetType);
    Map<Long, Long> getLikeCountMap(List<Long> targetIds, TargetType targetType);
    LikeResponse like(TargetType targetType, Long targetId);
    LikeResponse unlike(TargetType targetType, Long targetId);
    List<UserSummary> getLikeUsers(TargetType targetType, Long targetId);
}
