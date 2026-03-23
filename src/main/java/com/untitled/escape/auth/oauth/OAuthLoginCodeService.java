package com.untitled.escape.auth.oauth;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthLoginCodeService {
    private static final String PREFIX = "oauth:login-code:";
    private final StringRedisTemplate redisTemplate;

    @Value("${auth.oauth.login-code-expiration-seconds:60}")
    private long expirationSeconds;

    public String createCode(UUID userId) {
        String code = UUID.randomUUID().toString();
        String key = PREFIX + code;

        redisTemplate.opsForValue().set(
                key,
                userId.toString(),
                Duration.ofSeconds(expirationSeconds)
        );
        return code;
    }

    public UUID consume(String code) {
        String key = PREFIX + code;
        String userId = redisTemplate.opsForValue().get(key);

        if (userId == null || userId.isBlank()) {
            throw new RuntimeException("유효하지 않거나 만료된 OAuth 로그인 코드입니다.");
        }

        redisTemplate.delete(key);
        return UUID.fromString(userId);
    }

}
