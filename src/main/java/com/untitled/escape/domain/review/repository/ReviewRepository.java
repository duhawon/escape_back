package com.untitled.escape.domain.review.repository;

import com.untitled.escape.domain.review.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Slice<Review> findAllByRoom_IdOrderByCreatedAtDescIdDesc(Long roomId, Pageable pageable);
    Optional<Review> findByUserIdAndRoom_Id(UUID userId, Long roomId);
    boolean existsByUserIdAndRoom_Id(UUID userId, Long roomId);
    @Query("""
            select avg(r.rating)
            from Review r
            where r.room.id = :roomId
            """)
    Double findAverageRatingByRoomId(@Param("roomId") Long roomId);
}
