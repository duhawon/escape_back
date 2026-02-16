package com.untitled.escape.domain.review.dto;

import com.untitled.escape.domain.review.Review;
import com.untitled.escape.domain.user.dto.UserSummary;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.List;

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

    private List<ReviewCommentResponse> comments;

    public static ReviewDetailResponse of(
            Review review,
            UserSummary userSummary,
            Long likeCount,
            List<ReviewCommentResponse> commentResponses) {
        return ReviewDetailResponse.builder()
                .reviewId(review.getId())
                .roomId(review.getRoom().getId())
                .userSummary(userSummary)
                .content(review.getContent())
                .rating(review.getRating())
                .spoiler(review.isSpoiler())
                .likeCount(likeCount)
                .commentCount(commentResponses.size())
                .comments(commentResponses)
                .build();
    }
}
