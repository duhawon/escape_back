package com.untitled.escape.domain.review.service;

import com.untitled.escape.domain.like.TargetType;
import com.untitled.escape.domain.like.service.LikeService;
import com.untitled.escape.domain.review.Review;
import com.untitled.escape.domain.review.ReviewComment;
import com.untitled.escape.domain.review.dto.CreateReviewCommentRequest;
import com.untitled.escape.domain.review.dto.ReviewCommentResponse;
import com.untitled.escape.domain.review.dto.UpdateReviewCommentRequest;
import com.untitled.escape.domain.review.repository.ReviewCommentRepository;
import com.untitled.escape.domain.review.repository.ReviewRepository;
import com.untitled.escape.domain.user.dto.UserSummary;
import com.untitled.escape.domain.user.service.UserService;
import com.untitled.escape.global.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReviewCommentServiceImpl implements ReviewCommentService{
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final LikeService likeService;

    public ReviewCommentServiceImpl(ReviewCommentRepository reviewCommentRepository, ReviewRepository reviewRepository, UserService userService, LikeService likeService) {
        this.reviewCommentRepository = reviewCommentRepository;
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.likeService = likeService;
    }

    @Override
    @Transactional
    public void createComment(Long reviewId, CreateReviewCommentRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰 입니다."));
        UUID userId = SecurityUtils.getCurrentUserId();
        ReviewComment reviewComment = ReviewComment.builder()
                .userId(userId)
                .review(review)
                .content(request.getContent()).build();
        reviewCommentRepository.save(reviewComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewCommentResponse> getComments(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("존재하지 않는 리뷰 입니다.");
        }

        List<ReviewComment> comments = reviewCommentRepository.findAllByReview_Id(reviewId);

        if(comments.isEmpty()) return Collections.emptyList();

        // User Summary 조회를 위한 UserIds
        List<UUID> userIds = comments.stream()
                .map(ReviewComment::getUserId)
                .distinct()
                .toList();
        Map<UUID, UserSummary> userSummaryMap = userService.getUserSummaries(userIds);

        // LikeCount 조회를 위한 commentIds
        List<Long> commentIds = comments.stream()
                .map(ReviewComment::getId)
                .toList();
        Map<Long, Long> likeCountMap = likeService.getLikeCountMap(commentIds, TargetType.REVIEW_COMMENT);

        List<ReviewCommentResponse> commentResponses = comments.stream()
                .map(comment ->
                        ReviewCommentResponse.from(
                                comment,
                                userSummaryMap.get(comment.getUserId()),
                                likeCountMap.getOrDefault(comment.getId(),0L)
                        ))
                .toList();
        return commentResponses;
    }

    @Override
    @Transactional
    public void updateComment(Long reviewId, Long commentId, UpdateReviewCommentRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        ReviewComment reviewComment = reviewCommentRepository.findByIdAndReview_IdAndUserId(commentId, reviewId, userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 코멘트 입니다."));
        reviewComment.updateContent(request.getContent());
    }

    @Override
    @Transactional
    public void deleteComment(Long reviewId, Long commentId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        ReviewComment reviewComment = reviewCommentRepository.findByIdAndReview_IdAndUserId(commentId, reviewId, userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않은 코멘트 입니다."));
        reviewComment.softDelete();
    }
}
