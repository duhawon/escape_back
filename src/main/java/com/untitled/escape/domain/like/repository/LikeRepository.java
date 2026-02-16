package com.untitled.escape.domain.like.repository;

import com.untitled.escape.domain.like.Like;
import com.untitled.escape.domain.like.TargetType;
import com.untitled.escape.domain.like.repository.projection.TargetLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndTargetTypeAndTargetId(UUID userId, TargetType targetType, Long targetId);
    void deleteByUserIdAndTargetTypeAndTargetId(UUID userId, TargetType targetType, Long targetId);
    long countByTargetTypeAndTargetId(TargetType targetType, Long targetId);
    @Query("""
            select l.userId
            from Like l
            where l.targetId = :targetId
            and l.targetType = :targetType
            """)
    List<UUID> findUserIdsByTarget(@Param("targetType") TargetType targetType, @Param("targetId") Long targetId);

    @Query("""
            select l.targetId as targetId, count(l.id) as count
            from Like l
            where l.targetType = :targetType
            and l.targetId in :targetIds
            group by l.targetId
            """)
    List<TargetLikeCount> countGroupedByTargetIdAndTargetType(@Param("targetIds") List<Long> targetIds, @Param("targetType") TargetType targetType);
}
