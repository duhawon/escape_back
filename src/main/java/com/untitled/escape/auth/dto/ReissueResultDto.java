package com.untitled.escape.auth.dto;

import com.untitled.escape.domain.user.User;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReissueResultDto {
    private final String accessToken;
    private final String refreshToken;
    private final User user;
}
