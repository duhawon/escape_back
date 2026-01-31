package com.untitled.escape.domain.review;

import com.untitled.escape.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_review_user_room",
                        columnNames = {"user_id", "room_id"}
                )
        })
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "review",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<ReviewComment> comments = new ArrayList<>();

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "room_id", nullable = false, updatable = false)
    private Long roomId;

    @Size(max = 10000)
    @Column(columnDefinition = "TEXT")
    private String content;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "review_images",
            joinColumns = @JoinColumn(name = "review_id")
    )
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5.0")
    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rating;
}
