package com.untitled.escape.domain.review;

import com.untitled.escape.domain.room.Room;
import com.untitled.escape.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_review_user_room_active",
                        columnNames = {"user_id", "room_id","active_flag"}
                )
        })
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "review")
    @Builder.Default
    private List<ReviewComment> comments = new ArrayList<>();

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, updatable = false)
    private Room room;

    @Size(max = 10000)
    @Column(columnDefinition = "TEXT")
    private String content;

//    @ElementCollection(fetch = FetchType.LAZY)
//    @CollectionTable(
//            name = "review_images",
//            joinColumns = @JoinColumn(name = "review_id")
//    )
//    @Column(name = "image_url")
//    @Builder.Default
//    private List<String> imageUrls = new ArrayList<>();

    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5.0")
    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(name = "is_spoiler", nullable = false)
    private boolean spoiler;

    @Column(
            name="active_flag",
            insertable=false,
            updatable = false,
            columnDefinition =
                    "TINYINT GENERATED ALWAYS AS ((CASE WHEN deleted_at IS NULL THEN 1 ELSE NULL END)) PERSISTENT"
    )
    private Integer activeFlag;



    public void updateContent(String content) {
        this.content = content;
    }

    public void updateRating(BigDecimal rating) {
        this.rating = rating;
    }

    public void updateSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
    }
}
