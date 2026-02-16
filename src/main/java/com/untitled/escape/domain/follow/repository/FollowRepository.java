package com.untitled.escape.domain.follow.repository;

import com.untitled.escape.domain.follow.Follow;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("""
            select f
            from Follow f
            join fetch f.followee
            where f.follower.id = :userId
            """)
    List<Follow> findAllByFollower_Id(UUID userId);

    @Query("""
            select f
            from Follow f
            join fetch f.follower
            where f.followee.id = :userId
            """)
    List<Follow> findAllByFollowee_Id(UUID userId);

    void deleteByFollower_IdAndFollowee_Id(UUID userId, UUID targetUserId);

    boolean existsByFollower_IdAndFollowee_Id(UUID userId, UUID targetUserId);
}
