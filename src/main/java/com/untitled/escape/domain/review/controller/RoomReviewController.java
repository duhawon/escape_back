package com.untitled.escape.domain.review.controller;

import com.untitled.escape.domain.review.dto.ReviewDetailResponse;
import com.untitled.escape.domain.review.dto.ReviewSummaryResponse;
import com.untitled.escape.domain.review.service.ReviewService;
import com.untitled.escape.global.dto.response.SliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms/{roomId}/reviews")
public class RoomReviewController {
    private final ReviewService reviewService;

    public RoomReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/me")
    public ResponseEntity<ReviewDetailResponse> getMyReview(@PathVariable Long roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getMyReviewByRoom(roomId));
    }

    @GetMapping
    public SliceResponse<ReviewSummaryResponse> getRoomReviews(@PathVariable Long roomId,
                                                                     @PageableDefault(size = 10) Pageable pageable) {
        return SliceResponse.from(reviewService.getReviewsByRoom(roomId, pageable));
    }
}
