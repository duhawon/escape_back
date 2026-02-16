package com.untitled.escape.domain.review.controller;

import com.untitled.escape.domain.review.dto.ReviewDetailResponse;
import com.untitled.escape.domain.review.dto.ReviewSummaryResponse;
import com.untitled.escape.domain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<List<ReviewSummaryResponse>> getRoomReviews(@PathVariable Long roomId) {
        List<ReviewSummaryResponse> reviewSummaryResponses = reviewService.getReviewsByRoom(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewSummaryResponses);
    }
}
