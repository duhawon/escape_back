package com.untitled.escape.auth.oauth;

import com.untitled.escape.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OAuthLoginService oAuthLoginService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest){
        OidcUserService delegate = new OidcUserService();

        if("naver".equals(userRequest.getClientRegistration().getRegistrationId())){
            delegate.setOauth2UserService(new NaverOAuth2UserService());
        }

        OidcUser oidcUser = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId,
                userNameAttributeName,
                oidcUser.getClaims()
        );

        User user = oAuthLoginService.loadOrCreate(attributes);

        String nameAttributeKey =
                (userNameAttributeName == null || userNameAttributeName.isBlank())
                        ? IdTokenClaimNames.SUB
                        : userNameAttributeName;

        return new EscapeOidcUser(
                user.getId(),
                oidcUser.getAuthorities(),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                nameAttributeKey
        );
    }
}
