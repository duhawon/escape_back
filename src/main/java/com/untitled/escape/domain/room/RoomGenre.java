package com.untitled.escape.domain.room;

public enum RoomGenre {
    HORROR("공포"),
    THRILLER("스릴러"),
    MYSTERY("미스터리"),
    DRAMA("드라마"),
    ROMANCE("감성/로맨스"),
    COMIC("코믹"),
    FANTASY("판타지"),
    SF("SF"),
    ADVENTURE("모험"),
    ACTION("액션"),
    STEALTH("잠입/미션"),
    PUZZLE("문제방/아케이드"),
    HISTORICAL("역사/사극"),
    ADULT("성인"),
    HEALING("힐링/가족");

    private final String displayName;

    RoomGenre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}