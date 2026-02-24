package com.untitled.escape.domain.user.dto;

import java.util.UUID;

public record MyProfileResponse(
        UUID userId,
        String name,
        String email,
        String profileImgUrl,
        long followers,
        long following
) {}
