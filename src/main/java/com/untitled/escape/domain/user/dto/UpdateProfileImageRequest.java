package com.untitled.escape.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileImageRequest {
    @NotBlank
    private String profileImgKey;
}
