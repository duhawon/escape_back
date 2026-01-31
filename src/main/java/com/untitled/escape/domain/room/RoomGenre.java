package com.untitled.escape.domain.room;

public enum RoomGenre {
    HORROR("공포"),
    MYSTERY("미스터리"),
    THRILLER("스릴러"),
    ADVENTURE("모험"),
    FANTASY("판타지");

    private final String displayName;

    RoomGenre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
