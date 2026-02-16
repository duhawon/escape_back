package com.untitled.escape.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserSummary {
    private UUID userId;
    private String name;
    private String profileImgUrl;
}
