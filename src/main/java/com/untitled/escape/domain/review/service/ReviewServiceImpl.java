package com.untitled.escape.domain.review.service;

import com.untitled.escape.domain.like.TargetType;
import com.untitled.escape.domain.like.service.LikeService;
import com.untitled.escape.domain.review.Review;
import com.untitled.escape.domain.review.dto.*;
import com.untitled.escape.domain.review.repository.ReviewCommentRepository;
import com.untitled.escape.domain.review.repository.ReviewRepository;
import com.untitled.escape.domain.review.repository.projection.ReviewCommentCount;
import com.untitled.escape.domain.room.Room;
import com.untitled.escape.domain.room.repository.RoomRepository;
import com.untitled.escape.domain.user.dto.UserSummary;
import com.untitled.escape.domain.user.service.UserService;
import com.untitled.escape.global.security.SecurityUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final LikeService likeService;
    private final ReviewCommentRepository reviewCommentRepository;
    private final RoomRepository roomRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserService userService, LikeService likeService, ReviewCommentRepository reviewCommentRepository, RoomRepository roomRepository) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.likeService = likeService;
        this.reviewCommentRepository = reviewCommentRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public void createReview(CreateReviewRequest dto) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Long roomId = dto.getRoomId();

        if (!roomRepository.existsById(roomId)) {
            throw new RuntimeException("존재하지 않는 방입니다.");  // 404
        }

        if (reviewRepository.existsByUserIdAndRoom_Id(userId, roomId)) {
            throw new RuntimeException("이미 이 방에 리뷰를 작성했습니다.");
        }

        Room roomRef = roomRepository.getReferenceById(dto.getRoomId());
        Review review = Review.builder()
                .userId(userId)
                .room(roomRef)
                .content(dto.getContent())
                .rating(dto.getRating())
                .spoiler(dto.isSpoiler())
                .build();
        try {
            reviewRepository.save(review);
        } catch (DataIntegrityViolationException e) {
            // DESC : 동시요청 방어..?
            throw new RuntimeException("이미 이 방에 리뷰를 작성했습니다.");
        }
    }

    @Override
    @Transactional
    public void updateReview(Long reviewId, UpdateReviewRequest dto) {
        UUID userId = SecurityUtils.getCurrentUserId();
        // 1. 있는 리뷰인지 확인
        // TODO : CustomException으로 변경
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("존재하지 않은 리뷰 입니다."));
        // 2. 작성자 검증
        if (!review.getUserId().equals(userId)) {
            // TODO : CustomException으로 변경
            throw new RuntimeException("본인 리뷰만 수정 할 수 있습니다.");
        }
        // 3. 값 수정
        review.updateContent(dto.getContent());
        review.updateRating(dto.getRating());
        review.updateSpoiler(dto.isSpoiler());
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDetailResponse getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰 입니다."));

        UserSummary userSummary = userService.getUserSummary(review.getUserId());
        long likeCount = likeService.getLikeCount(review.getId(), TargetType.REVIEW);
        long commentCount = reviewCommentRepository.countByReview_Id(reviewId);
        return ReviewDetailResponse.of(review, userSummary, likeCount, commentCount);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDetailResponse getMyReviewByRoom(Long roomId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Review review = reviewRepository.findByUserIdAndRoom_Id(userId, roomId)
                .orElse(null);
        if (review == null) {
            return null;
        }

        UserSummary userSummary = userService.getUserSummary(review.getUserId());
        long likeCount = likeService.getLikeCount(review.getId(), TargetType.REVIEW);
        long commentCount = reviewCommentRepository.countByReview_Id(review.getId());

        return ReviewDetailResponse.of(review, userSummary, likeCount, commentCount);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<ReviewSummaryResponse> getReviewsByRoom(Long roomId, Pageable pageable, String sort) {
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        String s = (sort == null) ? "new" : sort.toLowerCase();

        Slice<Review> reviewSlice = switch (s) {
            case "old"  -> reviewRepository.findAllByRoom_IdOrderByCreatedAtAscIdAsc(roomId, p);
            case "high" -> reviewRepository.findAllByRoom_IdOrderByRatingDescIdDesc(roomId, p);
            case "low"  -> reviewRepository.findAllByRoom_IdOrderByRatingAscIdDesc(roomId, p);
            case "likes" -> reviewRepository.findAllByRoomIdOrderByLikeCountDesc(roomId, TargetType.REVIEW, p);
            default     -> reviewRepository.findAllByRoom_IdOrderByCreatedAtDescIdDesc(roomId, p);
        };

        if(reviewSlice.isEmpty()) {
            return new SliceImpl<>(List.of(), p, false);
        }
        List<Review> reviews = reviewSlice.getContent();
        List<UUID> userIds = reviews.stream()
                .map(Review::getUserId)
                .distinct()
                .toList();
        Map<UUID, UserSummary> userSummaryMap = userService.getUserSummaries(userIds);
        List<Long> reviewIds = reviews.stream()
                .map(Review::getId)
                .toList();
        Map<Long, Long> likeCountMap = likeService.getLikeCountMap(reviewIds, TargetType.REVIEW);
        List<ReviewCommentCount> reviewCommentCounts = reviewCommentRepository.countGroupedByReviewId(reviewIds);
        Map<Long, Long> reviewCommentCountMap = reviewCommentCounts.stream()
                .collect(Collectors.toMap(
                        ReviewCommentCount::getReviewId,
                        ReviewCommentCount::getCount));

        List<ReviewSummaryResponse> reviewSummaryResponses = reviews.stream()
                .map(review ->
                        ReviewSummaryResponse.of(
                                review,
                                userSummaryMap.get(review.getUserId()),
                                likeCountMap.getOrDefault(review.getId(), 0L),
                                reviewCommentCountMap.getOrDefault(review.getId(), 0L)
                        ))
                .toList();
        return new SliceImpl<>(reviewSummaryResponses, p, reviewSlice.hasNext());
    }
    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        // 1. 있는 리뷰인지 확인
        // TODO : CustomException으로 변경
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("존재하지 않은 리뷰 입니다."));
        // 2. 작성자 검증
        if (!review.getUserId().equals(userId)) {
            // TODO : CustomException으로 변경
            throw new RuntimeException("본인 리뷰만 삭제 할 수 있습니다.");
        }
        // 3. 값 수정
        review.softDelete();
        reviewCommentRepository.softDeleteAllByReviewId(reviewId);
    }
}
