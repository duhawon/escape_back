package com.untitled.escape.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponse {
    private long likeCount;
    private boolean liked;
}
