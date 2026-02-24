package com.untitled.escape.domain.user.dto;

import java.util.UUID;

public record UserProfileResponse(
        UUID userId,
        String name,
        String profileImgUrl,
        long followers,
        long following
) {}
