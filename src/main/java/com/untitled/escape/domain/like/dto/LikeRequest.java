package com.untitled.escape.domain.like.dto;

import com.untitled.escape.domain.like.TargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeRequest {
    private TargetType targetType;
    private Long targetId;
}
