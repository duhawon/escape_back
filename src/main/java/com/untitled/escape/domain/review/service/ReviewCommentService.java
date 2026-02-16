package com.untitled.escape.domain.review.service;

import com.untitled.escape.domain.review.dto.CreateReviewCommentRequest;
import com.untitled.escape.domain.review.dto.ReviewCommentResponse;
import com.untitled.escape.domain.review.dto.UpdateReviewCommentRequest;

import java.util.List;

public interface ReviewCommentService {
    void createComment(Long reviewId, CreateReviewCommentRequest request);

    List<ReviewCommentResponse> getComments(Long reviewId);

    void updateComment(Long reviewId, Long commentId, UpdateReviewCommentRequest request);

    void deleteComment(Long reviewId, Long commentId);
}
