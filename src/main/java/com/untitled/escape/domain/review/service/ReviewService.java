package com.untitled.escape.domain.review.service;

import com.untitled.escape.domain.review.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewService {
    void createReview(CreateReviewRequest requestDto);
    void updateReview(Long reviewId, UpdateReviewRequest requestDto);
    ReviewDetailResponse getReview(Long reviewId);
    void deleteReview(Long reviewId);
    ReviewDetailResponse getMyReviewByRoom(Long roomId);
    Slice<ReviewSummaryResponse> getReviewsByRoom(Long roomId, Pageable pageable);
}
