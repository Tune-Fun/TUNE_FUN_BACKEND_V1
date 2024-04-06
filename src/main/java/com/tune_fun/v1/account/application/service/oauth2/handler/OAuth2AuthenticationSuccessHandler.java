package com.tune_fun.v1.account.application.service.oauth2.handler;

import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.account.application.port.output.SaveAccountPort;
import com.tune_fun.v1.account.application.port.output.jwt.CreateAccessTokenPort;
import com.tune_fun.v1.account.application.port.output.jwt.CreateRefreshTokenPort;
import com.tune_fun.v1.account.application.port.output.oauth2.*;
import com.tune_fun.v1.account.domain.behavior.SaveAccount;
import com.tune_fun.v1.account.domain.behavior.SaveJwtToken;
import com.tune_fun.v1.account.domain.behavior.SaveOAuth2Account;
import com.tune_fun.v1.account.domain.state.Account;
import com.tune_fun.v1.account.domain.state.CurrentAccount;
import com.tune_fun.v1.account.domain.state.RegisteredAccount;
import com.tune_fun.v1.account.domain.state.oauth2.*;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.exception.OAuth2AuthenticationProcessingException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.util.StringUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import static com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AuthorizationRequestPersistenceAdapter.*;
import static com.tune_fun.v1.account.domain.state.oauth2.OAuth2AuthorizationRequestMode.fromQueryParameter;
import static com.tune_fun.v1.account.domain.state.oauth2.OAuth2Provider.APPLE;
import static com.tune_fun.v1.common.response.MessageCode.*;
import static com.tune_fun.v1.common.util.CookieUtil.getCookie;
import static com.tune_fun.v1.common.util.StringUtil.getFlattenAuthorities;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Slf4j
@Component
@UseCase
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String ACCESS_TOKEN_QUERY_PARAMETER = "access_token";
    private static final String REFRESH_TOKEN_QUERY_PARAMETER = "refresh_token";


    private final DeleteAuthorizationRequestPort deleteAuthorizationRequestPort;

    private final OAuth2ClientProperties oAuth2ClientProperties;

    private final LoadAccountPort loadAccountPort;

    private final SaveAccountPort saveAccountPort;
    private final SaveOAuth2AccountPort saveOAuth2AccountPort;

    private final DisableOAuth2AccountPort disableOAuth2AccountPort;

    private final CreateAccessTokenPort createAccessTokenPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;

    private final RevokeOAuth2GooglePort revokeOAuth2GooglePort;
    private final RevokeOAuth2ApplePort revokeOAuth2ApplePort;

    private static final Function<String, String> AUTH_FAILED_URL_FUNCTION = targetUrl ->
            fromUriString(targetUrl).queryParam("error", "Authorization failed")
                    .build().toUriString();

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.error("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Transactional
    public String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                     Authentication authentication) {
        Optional<String> redirectUri = getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        Optional<OAuth2UserPrincipal> principalOptional = evaluateAuthentication(authentication);
        Optional<String> modeOptional = getCookie(request, MODE_PARAM_COOKIE_NAME).map(Cookie::getValue);

        if (principalOptional.isEmpty() || modeOptional.isEmpty()) return AUTH_FAILED_URL_FUNCTION.apply(targetUrl);

        OAuth2UserPrincipal principal = principalOptional.get();
        OAuth2AuthorizationRequestMode mode = fromQueryParameter(modeOptional.get());

        return switch (mode) {
            case LOGIN -> login(principal, targetUrl);
            case LINK -> link(principal, targetUrl, request);
            case UNLINK -> unlink(principal, targetUrl, request);
        };
    }

    @Transactional
    public String login(final OAuth2UserPrincipal principal, final String targetUrl) {
        if (loadRegisteredOAuth2Account(principal).isEmpty()) {
            CurrentAccount currentAccount = saveBaseAccount(principal);
            saveOAuth2Account(principal, currentAccount);
        }

        return createJwtTokenAndRedirectUri(principal, targetUrl);
    }

    @Transactional
    public String link(final OAuth2UserPrincipal principal, final String targetUrl, HttpServletRequest request) {
        Optional<String> usernameOptional = getCookie(request, USERNAME_PARAM_COOKIE_NAME).map(Cookie::getValue);
        if (usernameOptional.isEmpty()) return AUTH_FAILED_URL_FUNCTION.apply(targetUrl);

        if (loadRegisteredOAuth2Account(principal).isPresent())
            throw new CommonApplicationException(USER_POLICY_ALREADY_LINKED_PROVIDER);

        String username = usernameOptional.get();
        RegisteredAccount registeredAccount = loadRegisteredAccount(username);
        saveOAuth2Account(principal, registeredAccount);

        return createJwtTokenAndRedirectUri(principal, targetUrl);
    }

    @NotNull
    private String createJwtTokenAndRedirectUri(final OAuth2UserPrincipal principal, final String targetUrl) {
        String authorities = getFlattenAuthorities(principal.getAuthorities());

        SaveJwtToken saveJwtTokenBehavior = new SaveJwtToken(principal.getUsername(), authorities);
        String accessToken = createAccessTokenPort.createAccessToken(saveJwtTokenBehavior);
        String refreshToken = createRefreshTokenPort.createRefreshToken(saveJwtTokenBehavior);

        return fromUriString(targetUrl)
                .queryParam(ACCESS_TOKEN_QUERY_PARAMETER, accessToken)
                .queryParam(REFRESH_TOKEN_QUERY_PARAMETER, refreshToken)
                .build().toUriString();
    }

    @Transactional
    public String unlink(final OAuth2UserPrincipal principal, final String targetUrl, HttpServletRequest request) {
        Optional<String> usernameOptional = getCookie(request, USERNAME_PARAM_COOKIE_NAME).map(Cookie::getValue);
        if (usernameOptional.isEmpty()) return AUTH_FAILED_URL_FUNCTION.apply(targetUrl);

        OAuth2UserInfo oAuth2UserInfo = principal.userInfo();

        String accessToken = oAuth2UserInfo.getAccessToken();
        OAuth2Provider provider = oAuth2UserInfo.getProvider();

        String username = usernameOptional.get();
        RegisteredAccount registeredAccount = loadRegisteredAccount(username);
        if (registeredAccount.isUniqueOAuth2Account())
            throw new CommonApplicationException(USER_POLICY_CANNOT_UNLINK_UNIQUE_PROVIDER);

        unlinkHttpRequest(provider, accessToken);
        disableOAuth2AccountPort.disableOAuth2Account(principal.userInfo().getEmail());

        return fromUriString(targetUrl).build().toUriString();
    }

    private Optional<OAuth2UserPrincipal> evaluateAuthentication(Authentication authentication) {
        return Optional.ofNullable(authentication.getPrincipal())
                .filter(principal -> principal instanceof OAuth2UserPrincipal)
                .map(OAuth2UserPrincipal.class::cast);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        deleteAuthorizationRequestPort.delete(request, response);
    }

    @Transactional
    public RegisteredAccount loadRegisteredAccount(final String username) {
        return loadAccountPort.registeredAccountInfoByUsername(username)
                .orElseThrow(() -> new CommonApplicationException(ACCOUNT_NOT_FOUND));
    }

    @Transactional
    public Optional<RegisteredOAuth2Account> loadRegisteredOAuth2Account(OAuth2UserPrincipal principal) {
        return loadAccountPort.registeredOAuth2AccountInfoByEmail(principal.userInfo().getEmail());
    }

    @Transactional
    public CurrentAccount saveBaseAccount(final OAuth2UserPrincipal principal) {
        SaveAccount saveAccountBehavior = new SaveAccount(StringUtil.uuid(), principal.userInfo().getEmail(),
                "social", principal.userInfo().getEmail(), principal.userInfo().getNickname(),
                true, true, true);
        return saveAccountPort.saveAccount(saveAccountBehavior);
    }

    @Transactional
    public void saveOAuth2Account(final OAuth2UserPrincipal principal, final Account account) {
        SaveOAuth2Account saveOAuth2AccountBehavior = new SaveOAuth2Account(
                principal.userInfo().getEmail(),
                principal.userInfo().getNickname(),
                principal.userInfo().getProvider().name(),
                account.username()
        );
        saveOAuth2AccountPort.saveOAuth2Account(saveOAuth2AccountBehavior);
    }

    private void unlinkHttpRequest(final OAuth2Provider provider, final String accessToken) {
        if (accessToken == null || accessToken.trim().isEmpty())
            throw new OAuth2AuthenticationProcessingException("Access token must not be null or empty");

        switch (provider) {
            case GOOGLE -> revokeOAuth2GooglePort.revokeOAuth2Google(accessToken);
            case APPLE -> {
                RevokeOAuth2AppleRequest request = getRevokeOAuth2AppleRequest(accessToken);
                revokeOAuth2ApplePort.revokeOAuth2Apple(request);
            }
            default -> throw new OAuth2AuthenticationProcessingException(
                    "Unlink with " + provider.getRegistrationId() + " is not supported");
        }
    }

    @NotNull
    private RevokeOAuth2AppleRequest getRevokeOAuth2AppleRequest(final String accessToken) {
        OAuth2ClientProperties.Registration registration = oAuth2ClientProperties
                .getRegistration()
                .get(APPLE.getRegistrationId());

        if (registration == null) throw new OAuth2AuthenticationProcessingException("Apple registration not found");

        return new RevokeOAuth2AppleRequest(registration.getClientId(), registration.getClientSecret(), accessToken);
    }

}
