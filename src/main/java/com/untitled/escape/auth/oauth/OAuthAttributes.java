package com.untitled.escape.auth.oauth;

import com.untitled.escape.global.exception.CustomException;
import com.untitled.escape.global.exception.code.AuthErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {

    private final OAuthProvider provider;
    private final String providerUserId;
    private final String email;
    private final String name;
    private final Map<String, Object> attributes;

    public static OAuthAttributes of(
            String registrationId,
            String userNameAttributeName,
            Map<String, Object> attributes
    ) {
        if("google".equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        if ("naver".equals(registrationId)) {
            return ofNaver(userNameAttributeName, attributes);
        }
        throw new CustomException(AuthErrorCode.OAUTH2_PROVIDER_NOT_SUPPORTED);
    }

    private static OAuthAttributes ofNaver(
            String userNameAttributeName,
            Map<String, Object> attributes
    ) {
        Object providerUserId = attributes.get(userNameAttributeName);

        return OAuthAttributes.builder()
                .provider(OAuthProvider.NAVER)
                .providerUserId(providerUserId != null ? String.valueOf(providerUserId) : null)
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .attributes(attributes)
                .build();
    }

    private static OAuthAttributes ofKakao(
            String userNameAttributeName,
            Map<String, Object> attributes) {
        Object providerUserId = attributes.get(userNameAttributeName);

        return OAuthAttributes.builder()
                .provider(OAuthProvider.KAKAO)
                .providerUserId(providerUserId != null ? String.valueOf(providerUserId) : null)
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .attributes(attributes)
                .build();
    }

    private static OAuthAttributes ofGoogle(
            String userNameAttributeName,
            Map<String, Object> attributes
    ) {
        Object providerUserId = attributes.get(userNameAttributeName);

        return OAuthAttributes.builder()
                .provider(OAuthProvider.GOOGLE)
                .providerUserId(providerUserId != null ? String.valueOf(providerUserId) : null)
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .attributes(attributes)
                .build();
    }
}
