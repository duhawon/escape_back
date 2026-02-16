package com.untitled.escape.domain.review.repository;

import com.untitled.escape.domain.review.ReviewComment;
import com.untitled.escape.domain.review.repository.projection.ReviewCommentCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    List<ReviewComment> findAllByReview_Id(Long reviewId);

    @Query("""
            select rc.review.id as reviewId, count(rc.id) as count
            from ReviewComment rc
            where rc.review.id in :reviewIds
            group by rc.review.id
            """)
    List<ReviewCommentCount> countGroupedByReviewId(@Param("reviewIds") List<Long> reviewIds);
    Optional<ReviewComment> findByIdAndReview_IdAndUserId(Long commentId, Long reviewId, UUID userId);
    @Modifying(clearAutomatically = true, flushAutomatically = true) // DESC : 벌크 변경 전에 기존변경사항부터 db반영, 벌크 변경후 메모리 캐시(영속성 컨텍스트) 비움
    @Query("""
            update ReviewComment rc
            set rc.deletedAt = CURRENT_TIMESTAMP
            where rc.review.id = :reviewId
            and rc.deletedAt is null
            """)
    int softDeleteAllByReviewId(@Param("reviewId") Long reviewId);
}
