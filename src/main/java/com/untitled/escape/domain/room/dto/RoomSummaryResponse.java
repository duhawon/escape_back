package com.untitled.escape.domain.room.dto;

import com.untitled.escape.domain.room.repository.projection.RoomSummaryProjection;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class RoomSummaryResponse {
    private final Long id;
    private final String name;
    private final BigDecimal rating;

    private RoomSummaryResponse(Long id, String name, BigDecimal rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public static RoomSummaryResponse from(RoomSummaryProjection projection) {
        BigDecimal rating = projection.getAverageRating() == null
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(projection.getAverageRating())
                            .setScale(1, RoundingMode.HALF_UP);
        return new RoomSummaryResponse(projection.getId(), projection.getName(), rating);
    }
}
