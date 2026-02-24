package com.untitled.escape.domain.follow.repository;

import com.untitled.escape.domain.follow.Follow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // DESC : 내가 팔로우 한 사람들
    @Query("""
            select f.followee.id
            from Follow f
            where f.follower.id = :userId
            order by f.createdAt desc, f.id desc
            """)
    Slice<UUID> findFollowingIds(@Param("userId") UUID userId, Pageable pageable);
    // DESC : 나를 팔로우 하는 사람들
    @Query("""
            select f.follower.id
            from Follow f
            where f.followee.id = :userId
            order by f.createdAt desc, f.id desc
            """)
    Slice<UUID> findFollowerIds(@Param("userId") UUID userId, Pageable pageable);

    void deleteByFollower_IdAndFollowee_Id(UUID userId, UUID targetUserId);

    @Query("""
            select f.followee.id
            from Follow f
            where f.follower.id = :followerId
            and f.followee.id in :followeeIds
            """)
    List<UUID> findFollowingIdsIn(@Param("followerId") UUID followerId,
                                  @Param("followeeIds") List<UUID> followeeIds);

    long countByFollowee_Id(UUID userId);
    long countByFollower_Id(UUID userId);
}
