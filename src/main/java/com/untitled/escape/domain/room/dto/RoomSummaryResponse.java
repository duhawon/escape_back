package com.untitled.escape.domain.room.dto;

import com.untitled.escape.domain.room.repository.projection.RoomSummaryProjection;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Builder
public class RoomSummaryResponse {
    private final Long id;
    private final String name;
    private final BigDecimal rating;
    private final String posterImgUrl;

    public static RoomSummaryResponse from(RoomSummaryProjection projection, String posterImgUrl) {
        BigDecimal rating = projection.getAverageRating() == null
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(projection.getAverageRating())
                            .setScale(1, RoundingMode.HALF_UP);
        return RoomSummaryResponse.builder()
                .id(projection.getId())
                .name(projection.getName())
                .rating(rating)
                .posterImgUrl(posterImgUrl)
                .build();    }
}
