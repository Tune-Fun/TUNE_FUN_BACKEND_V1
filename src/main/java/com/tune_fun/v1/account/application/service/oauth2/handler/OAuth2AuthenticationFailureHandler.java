package com.tune_fun.v1.account.application.service.oauth2.handler;

import com.tune_fun.v1.account.application.port.output.oauth2.RemoveAuthorizationRequestCookiePort;
import com.tune_fun.v1.common.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.tune_fun.v1.account.adapter.output.persistence.oauth2.HttpCookieOAuth2AuthorizationRequestPersistenceAdapter.REDIRECT_URI_PARAM_COOKIE_NAME;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final RemoveAuthorizationRequestCookiePort removeAuthorizationRequestCookiePort;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(("/"));

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        removeAuthorizationRequestCookiePort.remove(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
