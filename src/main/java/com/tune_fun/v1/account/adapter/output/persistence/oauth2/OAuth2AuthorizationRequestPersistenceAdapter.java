package com.tune_fun.v1.account.adapter.output.persistence.oauth2;

import com.tune_fun.v1.account.application.port.output.oauth2.DeleteAuthorizationRequestPort;
import com.tune_fun.v1.common.stereotype.PersistenceAdapter;
import com.tune_fun.v1.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import static com.tune_fun.v1.common.util.CookieUtil.*;
import static org.springframework.util.StringUtils.hasText;


@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class OAuth2AuthorizationRequestPersistenceAdapter
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest>, DeleteAuthorizationRequestPort {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String MODE_PARAM_COOKIE_NAME = "mode";
    public static final String USERNAME_PARAM_COOKIE_NAME = "username";
    public static final int COOKIE_EXPIRE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtil.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
                                         HttpServletResponse response) {
        if (authorizationRequest == null) {
            delete(request, response);
            return;
        }

        addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);

        checkRequestParameterAndAddCookie(REDIRECT_URI_PARAM_COOKIE_NAME, request, response);
        checkRequestParameterAndAddCookie(MODE_PARAM_COOKIE_NAME, request, response);
        checkRequestParameterAndAddCookie(USERNAME_PARAM_COOKIE_NAME, request, response);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    @Override
    public void delete(HttpServletRequest request, HttpServletResponse response) {
        removeAuthorizationRequestCookies(request, response);
    }

    private static void checkRequestParameterAndAddCookie(final String parameterName, HttpServletRequest request, HttpServletResponse response) {
        String parameterValue = request.getParameter(parameterName);
        if (hasText(parameterValue))
            addCookie(response, parameterName, parameterValue, COOKIE_EXPIRE_SECONDS);
    }

    private static void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        deleteCookie(request, response, MODE_PARAM_COOKIE_NAME);
        deleteCookie(request, response, USERNAME_PARAM_COOKIE_NAME);
    }
}
