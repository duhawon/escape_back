package com.untitled.escape.domain.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateReviewRequest {
    @NotNull
    private Long roomId;
    private BigDecimal rating;
    private String content;
    private boolean spoiler;
    private List<String> imageUrls;
}
