package com.untitled.escape.domain.review.dto;

import com.untitled.escape.domain.review.ReviewComment;
import com.untitled.escape.domain.user.dto.UserSummary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewCommentResponse {
    private Long commentId;
    private UserSummary userSummary;
    private String content;
    private long likeCount;
    private LocalDateTime createdAt;

    private boolean likedByMe;

    public static ReviewCommentResponse from(
            ReviewComment comment,
            UserSummary userSummary,
            long likeCount,
            boolean likedByMe
    ) {
        return ReviewCommentResponse.builder()
                .commentId(comment.getId())
                .userSummary(userSummary)
                .content(comment.getContent())
                .likeCount(likeCount)
                .createdAt(comment.getCreatedAt())
                .likedByMe(likedByMe)
                .build();
    }
}
