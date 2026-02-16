package com.untitled.escape.domain.review.service;

import com.untitled.escape.domain.review.Review;
import com.untitled.escape.domain.review.dto.*;

import java.util.List;

public interface ReviewService {
    void createReview(CreateReviewRequest requestDto);
    void updateReview(Long reviewId, UpdateReviewRequest requestDto);
    ReviewDetailResponse getReview(Long reviewId);
    void deleteReview(Long reviewId);
    ReviewDetailResponse getMyReviewByRoom(Long roomId);
    List<ReviewSummaryResponse> getReviewsByRoom(Long roomId);
}
