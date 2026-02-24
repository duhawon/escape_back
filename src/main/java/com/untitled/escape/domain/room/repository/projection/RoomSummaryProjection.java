package com.untitled.escape.domain.room.repository.projection;

public interface RoomSummaryProjection {
    Long getId();
    String getName();
    Double getAverageRating();
    String getPosterImgKey();
}
