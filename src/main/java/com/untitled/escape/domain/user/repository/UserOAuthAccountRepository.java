package com.untitled.escape.domain.user.repository;

import com.untitled.escape.domain.user.OAuthProvider;
import com.untitled.escape.domain.user.UserOAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserOAuthAccountRepository extends JpaRepository<UserOAuthAccount, UUID> {
    Optional<UserOAuthAccount> findByProviderAndProviderUserId(OAuthProvider provider, String providerUserId);
}
