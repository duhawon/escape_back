package com.untitled.escape.auth.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuthLoginCodeService oAuthLoginCodeService;

    @Value("${app.frontend-base-url}")
    private String frontedBaseUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        EscapeOidcUser principle = (EscapeOidcUser) authentication.getPrincipal();
        String loginCode = oAuthLoginCodeService.createCode(principle.getUserId());

        clearAuthenticationAttributes(request);
        String redirectUrl = frontedBaseUrl + "/oauth/callback?code="
                + URLEncoder.encode(loginCode, StandardCharsets.UTF_8);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
