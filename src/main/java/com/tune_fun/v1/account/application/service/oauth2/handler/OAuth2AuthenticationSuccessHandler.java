package com.tune_fun.v1.account.application.service.oauth2.handler;

import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.SaveAccountPort;
import com.tune_fun.v1.account.application.port.output.jwt.CreateAccessTokenPort;
import com.tune_fun.v1.account.application.port.output.jwt.CreateRefreshTokenPort;
import com.tune_fun.v1.account.application.port.output.oauth2.RemoveAuthorizationRequestCookiePort;
import com.tune_fun.v1.account.application.port.output.oauth2.SaveOAuth2AccountPort;
import com.tune_fun.v1.account.application.service.oauth2.OAuth2UserPrincipal;
import com.tune_fun.v1.account.application.service.oauth2.unlink.OAuth2UserUnlinkFacade;
import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.behavior.SaveJwtToken;
import com.tune_fun.v1.account.domain.behavior.SaveOAuth2Account;
import com.tune_fun.v1.account.domain.state.CurrentAccount;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.util.CookieUtil;
import com.tune_fun.v1.common.util.StringUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static com.tune_fun.v1.account.adapter.output.persistence.oauth2.HttpCookieOAuth2AuthorizationRequestPersistenceAdapter.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@UseCase
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RemoveAuthorizationRequestCookiePort removeAuthorizationRequestCookiePort;

    private final LoadAccountPort loadAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final SaveOAuth2AccountPort saveOAuth2AccountPort;
    private final CreateAccessTokenPort createAccessTokenPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl;

        targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Transactional
    public String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();
        }

        checkRegisteredAccount(principal);
        CurrentAccount currentAccount = saveBaseAccount(principal);
        saveOAuth2Account(principal, currentAccount);

        String authorities = StringUtil.getFlattenAuthorities(principal.getAuthorities());

        SaveJwtToken saveJwtTokenBehavior = new SaveJwtToken(principal.getUsername(), authorities);
        String accessToken = createAccessTokenPort.createAccessToken(saveJwtTokenBehavior);
        String refreshToken = createRefreshTokenPort.createRefreshToken(saveJwtTokenBehavior);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString();

    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) return (OAuth2UserPrincipal) principal;
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        removeAuthorizationRequestCookiePort.remove(request, response);
    }

    @Transactional
    public void checkRegisteredAccount(final OAuth2UserPrincipal principal) {
        loadAccountPort.registeredAccountInfoByUsername(principal.userInfo().getEmail())
                .ifPresent(account -> {
                    throw new CommonApplicationException(MessageCode.USER_POLICY_ACCOUNT_REGISTERED);
                });
    }

    @Transactional
    public CurrentAccount saveBaseAccount(final OAuth2UserPrincipal principal) {
        SaveAccount saveAccountBehavior = new SaveAccount(StringUtil.uuid(), principal.userInfo().getEmail(),
                "social", principal.userInfo().getEmail(), principal.userInfo().getName(),
                true, true, true);
        return saveAccountPort.saveAccount(saveAccountBehavior);
    }

    @Transactional
    public void saveOAuth2Account(final OAuth2UserPrincipal principal, final CurrentAccount currentAccount) {
        SaveOAuth2Account saveOAuth2AccountBehavior = new SaveOAuth2Account(
                principal.userInfo().getEmail(),
                principal.userInfo().getName(),
                principal.userInfo().getProvider().name(),
                currentAccount.username()
        );
        saveOAuth2AccountPort.saveOAuth2Account(saveOAuth2AccountBehavior);
    }

}
