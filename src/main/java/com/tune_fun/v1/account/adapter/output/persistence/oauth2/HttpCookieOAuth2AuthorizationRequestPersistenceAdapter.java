package com.tune_fun.v1.account.adapter.output.persistence.oauth2;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.account.application.port.output.oauth2.RemoveAuthorizationRequestCookiePort;
import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@XRayEnabled
@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class HttpCookieOAuth2AuthorizationRequestPersistenceAdapter
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest>, RemoveAuthorizationRequestCookiePort {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String MODE_PARAM_COOKIE_NAME = "mode";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtil.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
                                         HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            CookieUtil.deleteCookie(request, response, MODE_PARAM_COOKIE_NAME);
            return;
        }

        CookieUtil.addCookie(response,
                OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtil.serialize(authorizationRequest),
                COOKIE_EXPIRE_SECONDS);

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.hasText(redirectUriAfterLogin)) {
            CookieUtil.addCookie(response,
                    REDIRECT_URI_PARAM_COOKIE_NAME,
                    redirectUriAfterLogin,
                    COOKIE_EXPIRE_SECONDS);
        }

        String mode = request.getParameter(MODE_PARAM_COOKIE_NAME);
        if (StringUtils.hasText(mode)) {
            CookieUtil.addCookie(response,
                    MODE_PARAM_COOKIE_NAME,
                    mode,
                    COOKIE_EXPIRE_SECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    private void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        CookieUtil.deleteCookie(request, response, MODE_PARAM_COOKIE_NAME);
    }

    @Override
    public void remove(HttpServletRequest request, HttpServletResponse response) {
        removeAuthorizationRequestCookies(request, response);
    }
}
