package com.untitled.escape.auth.oauth;

import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.UserOAuthAccount;
import com.untitled.escape.domain.user.repository.UserOAuthAccountRepository;
import com.untitled.escape.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final UserOAuthAccountRepository userOAuthAccountRepository;

    public User loadOrCreate(OAuthAttributes attributes) {
        return userOAuthAccountRepository
                .findByProviderAndProviderUserId(
                        attributes.getProvider(),
                        attributes.getProviderUserId()
                )
                .map(UserOAuthAccount::getUser)
                .orElseGet(() -> registerNewOAuthUser(attributes));
    }

    private User registerNewOAuthUser(OAuthAttributes attributes) {
        if (attributes.getProviderUserId() == null || attributes.getProviderUserId().isBlank()) {
            throw new RuntimeException("providerUserId가 없습니다.");
        }

        if (attributes.getEmail() == null || attributes.getEmail().isBlank()) {
            throw new RuntimeException("OAuth 이메일 정보가 없습니다.");
        }

        // 기존 로컬 계정이 있더라도 자동 병합 X
        userRepository.findByEmail(attributes.getEmail())
                .ifPresent(existing -> {
                    throw new RuntimeException(
                            "이미 같은 이메일의 일반 계정이 있습니다."
                    );
                });

        String name = (attributes.getName() == null || attributes.getName().isBlank())
                ? "사용자"
                : attributes.getName();

        User user = User.createOAuth(attributes.getEmail(), name);
        User savedUser = userRepository.save(user);

        UserOAuthAccount oAuthAccount = UserOAuthAccount.of(
                savedUser,
                attributes.getProvider(),
                attributes.getProviderUserId()
        );
        userOAuthAccountRepository.save(oAuthAccount);

        return savedUser;
    }

}
