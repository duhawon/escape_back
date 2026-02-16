package com.untitled.escape.domain.room.repository;

import com.untitled.escape.domain.room.repository.projection.RoomSummaryProjection;
import com.untitled.escape.domain.room.Room;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("""
            select r.id as id,
                   r.name as name,
                   avg(rv.rating) as averageRating
            from Room r
            left join Review rv on rv.roomId = r.id
            where (:query is null or r.name like concat('%', :query, '%'))
            group by r.id, r.name
            order by coalesce(avg(rv.rating), 0) desc
            """)
    List<RoomSummaryProjection> findRoomSummaries(@Param("query") String query);
}
