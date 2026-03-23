package com.untitled.escape.auth.oauth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;
import java.util.UUID;

@Getter
public class EscapeOidcUser extends DefaultOidcUser {
    private final UUID userId;

    public EscapeOidcUser(
            UUID userId,
            Collection<? extends GrantedAuthority> authorities,
            OidcIdToken idToken,
            OidcUserInfo userInfo,
            String nameAttributeKey
    ) {
        super(authorities, idToken, userInfo, nameAttributeKey);
        this.userId = userId;
    }
}
