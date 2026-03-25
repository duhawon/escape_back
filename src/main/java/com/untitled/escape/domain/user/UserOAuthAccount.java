package com.untitled.escape.domain.user;

import com.untitled.escape.auth.oauth.OAuthProvider;
import com.untitled.escape.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "user_oauth_accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_oauth_provider_user",
                        columnNames = {"provider", "provider_user_id"}
                ),
                @UniqueConstraint(
                        name = "uk_oauth_user_provider",
                        columnNames = {"user_id", "provider"}
                )
        })
public class UserOAuthAccount extends BaseEntity {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 20)
    private OAuthProvider provider;

    @Column(name = "provider_user_id", nullable = false, length = 255)
    private String providerUserId;

    @Builder(access = AccessLevel.PRIVATE)
    private UserOAuthAccount(User user, OAuthProvider provider, String providerUserId) {
        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
    }

    public static UserOAuthAccount of(User user, OAuthProvider provider, String providerUserId) {
        if (providerUserId == null || providerUserId.isBlank()) {
            throw new RuntimeException("providerUserId는 필수입니다.");
        }

        return UserOAuthAccount.builder()
                .user(user)
                .provider(provider)
                .providerUserId(providerUserId)
                .build();
    }
}
