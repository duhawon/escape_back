package com.untitled.escape.domain.review.controller;

import com.untitled.escape.domain.review.Review;
import com.untitled.escape.domain.review.dto.ReviewDetailResponse;
import com.untitled.escape.domain.review.dto.ReviewSummaryResponse;
import com.untitled.escape.domain.review.service.ReviewService;
import com.untitled.escape.global.dto.response.SliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms/{roomId}/reviews")
public class RoomReviewController {
    private final ReviewService reviewService;

    public RoomReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/me")
    public ResponseEntity<ReviewDetailResponse> getMyReview(@PathVariable Long roomId) {
        // TODO : 리뷰가 없을경우 처리방식 개선 필요
        ReviewDetailResponse res = reviewService.getMyReviewByRoom(roomId);
        if (res == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public SliceResponse<ReviewSummaryResponse> getRoomReviews(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "new") String sort,
            @PageableDefault(size = 10) Pageable pageable) {
        return SliceResponse.from(reviewService.getReviewsByRoom(roomId, pageable, sort));
    }
}
