package com.untitled.escape.domain.review.controller;

import com.untitled.escape.domain.review.dto.CreateReviewRequest;
import com.untitled.escape.domain.review.dto.ReviewDetailResponse;
import com.untitled.escape.domain.review.dto.ReviewSummaryResponse;
import com.untitled.escape.domain.review.dto.UpdateReviewRequest;
import com.untitled.escape.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Void> createReview(
            @RequestBody CreateReviewRequest requestDto) {
        reviewService.createReview(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("/{reviewId}")
    public  ResponseEntity<Void> updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid UpdateReviewRequest requestDto) {
        reviewService.updateReview(reviewId, requestDto);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDetailResponse> getReview(
            @PathVariable Long reviewId) {
        ReviewDetailResponse response = reviewService.getReview(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
