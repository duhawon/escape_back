package com.untitled.escape.domain.review.controller;

import com.untitled.escape.domain.review.dto.CreateReviewCommentRequest;
import com.untitled.escape.domain.review.dto.ReviewCommentResponse;
import com.untitled.escape.domain.review.dto.UpdateReviewCommentRequest;
import com.untitled.escape.domain.review.service.ReviewCommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews/{reviewId}/comments")
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;

    public ReviewCommentController(ReviewCommentService reviewCommentService) {
        this.reviewCommentService = reviewCommentService;
    }

    @PostMapping
    public ResponseEntity<Void> createComment(@PathVariable Long reviewId,
                              @RequestBody @Valid CreateReviewCommentRequest request) {
        reviewCommentService.createComment(reviewId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ReviewCommentResponse>> getComments(@PathVariable Long reviewId) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewCommentService.getComments(reviewId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long reviewId,
                              @PathVariable Long commentId,
                              @RequestBody @Valid UpdateReviewCommentRequest request) {
        reviewCommentService.updateComment(reviewId, commentId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long reviewId,
                              @PathVariable Long commentId) {
        reviewCommentService.deleteComment(reviewId, commentId);
        return ResponseEntity.noContent().build();
    }
}
