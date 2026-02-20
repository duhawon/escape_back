package com.untitled.escape.domain.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class UpdateReviewRequest {
    private BigDecimal rating;
    private String content;
    private boolean spoiler;
}
