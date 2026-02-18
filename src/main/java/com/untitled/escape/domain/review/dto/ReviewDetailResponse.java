package com.untitled.escape.domain.review.dto;

import com.untitled.escape.domain.review.Review;
import com.untitled.escape.domain.user.dto.UserSummary;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class ReviewDetailResponse {
    private Long reviewId;
    private Long roomId;
    private UserSummary userSummary;

    private String content;
    private BigDecimal rating;
    private boolean spoiler;

    private long likeCount;
    private long commentCount;

    public static ReviewDetailResponse of(
            Review review,
            UserSummary userSummary,
            long likeCount,
            long commentCount) {
        return ReviewDetailResponse.builder()
                .reviewId(review.getId())
                .roomId(review.getRoom().getId())
                .userSummary(userSummary)
                .content(review.getContent())
                .rating(review.getRating())
                .spoiler(review.isSpoiler())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }
}
