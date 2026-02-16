package com.untitled.escape.domain.room.dto;

import com.untitled.escape.domain.room.Room;
import com.untitled.escape.domain.room.RoomGenre;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Builder
public class RoomDetailResponse {
    private Long id;
    private String name;
    private BigDecimal rating;

    private Set<RoomGenre> genres;
    private BigDecimal difficulty;
    private int playTimeMinutes;
    private int minPlayers;
    private int maxPlayers;
    private String storeName;
    private String description;

    public static RoomDetailResponse from(Room room, BigDecimal rating) {
        return RoomDetailResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .rating(rating)
                .genres(room.getGenres())
                .difficulty(room.getDifficulty())
                .playTimeMinutes(room.getPlayTimeMinutes())
                .minPlayers(room.getMinPlayers())
                .maxPlayers(room.getMaxPlayers())
                .storeName(room.getStoreName())
                .description(room.getDescription())
                .build();
    }
}
