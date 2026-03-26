package com.untitled.escape.domain.user;

import com.untitled.escape.global.entity.BaseEntity;
import com.untitled.escape.global.exception.CustomException;
import com.untitled.escape.global.exception.code.UserErrorCode;
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
    @Column(name = "password")
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

    public static User createLocal(String email, String password, String name) {
        if (password == null || password.isBlank()) {
            throw new CustomException(UserErrorCode.LOCAL_ACCOUNT_PASSWORD_REQUIRED);
        }
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
    public static User createOAuth(String email, String name) {
        return User.builder()
                .email(email)
                .password(null)
                .name(name)
                .build();
    }
}
