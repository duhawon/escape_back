package com.untitled.escape.domain.review.dto;

import com.untitled.escape.domain.review.Review;
import com.untitled.escape.domain.user.dto.UserSummary;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class ReviewSummaryResponse {
    private Long reviewId;
    private UserSummary userSummary;

    private String content;
    private BigDecimal rating;
    private boolean spoiler;

    private long likeCount;
    private long commentCount;

    public static ReviewSummaryResponse of(
            Review review,
            UserSummary userSummary,
            Long likeCount,
            Long commentCount) {
        return ReviewSummaryResponse.builder()
                .reviewId(review.getId())
                .userSummary(userSummary)
                .content(review.getContent())
                .rating(review.getRating())
                .spoiler(review.isSpoiler())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }
}
