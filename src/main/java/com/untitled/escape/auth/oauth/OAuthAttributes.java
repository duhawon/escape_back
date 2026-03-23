package com.untitled.escape.auth.oauth;

import com.untitled.escape.domain.user.OAuthProvider;
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
        throw new RuntimeException("지원하지 않는 registrationId: " + registrationId);
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
