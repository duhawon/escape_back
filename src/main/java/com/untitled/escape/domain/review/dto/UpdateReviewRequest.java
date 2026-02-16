package com.untitled.escape.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class UpdateReviewRequest {
    @NotNull
    private BigDecimal rating;
    @NotBlank
    private String content;
    private boolean spoiler;
}
