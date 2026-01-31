package com.untitled.escape.domain.room;

import com.untitled.escape.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rooms")
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "room_genres",
            joinColumns = @JoinColumn(name = "room_id")
    )
    @Builder.Default
    @Column(name = "genre", nullable = false)
    private Set<RoomGenre> genres = new HashSet<>();

    @Column(nullable = false, precision = 2, scale = 1) // DESC: precision(전체 자릿수), scale(소수점 이하 자릿수)
    @DecimalMin("0.5")
    @DecimalMax("5.0")
    private BigDecimal difficulty;

    @Column(name = "play_time_minutes", nullable = false)
    private int playTimeMinutes;

    @Column(name = "min_players", nullable = false)
    private int minPlayers;
    @Column(name = "max_players", nullable = false)
    private int maxPlayers;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;
}
