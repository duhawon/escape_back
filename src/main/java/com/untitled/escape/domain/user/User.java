package com.untitled.escape.domain.user;

import com.untitled.escape.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="users")
public class User extends BaseEntity {
    @Id
    @UuidGenerator
    private UUID id;

    @Setter
    @Column(name = "email", nullable = false)
    private String email;

    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "profile_img_key")
    private String profileImgKey;

    public void updateProfileImgKey(String profileImgKey) {
        this.profileImgKey = profileImgKey;
    }
}
