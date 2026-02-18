package com.untitled.escape.domain.review.service;

import com.untitled.escape.domain.review.dto.CreateReviewCommentRequest;
import com.untitled.escape.domain.review.dto.ReviewCommentResponse;
import com.untitled.escape.domain.review.dto.UpdateReviewCommentRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewCommentService {
    void createComment(Long reviewId, CreateReviewCommentRequest request);

    Slice<ReviewCommentResponse> getComments(Long reviewId, Pageable pageable);

    void updateComment(Long reviewId, Long commentId, UpdateReviewCommentRequest request);

    void deleteComment(Long reviewId, Long commentId);
}
