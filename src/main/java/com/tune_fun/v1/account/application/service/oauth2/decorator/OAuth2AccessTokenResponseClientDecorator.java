package com.tune_fun.v1.account.application.service.oauth2.decorator;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.function.Predicate;

import static com.tune_fun.v1.account.domain.value.oauth2.OAuth2Provider.INSTAGRAM;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

/**
 * @see <a href="https://velog.io/@shdrnrhd113/인스타그램-소셜-로그인-구현하기">인스타그램 소셜 로그인 구현하기</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AccessTokenResponseClientDecorator implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>, InitializingBean {

    private final OAuth2AuthorizationCodeGrantRequestEntityDecorator oAuth2AuthorizationCodeGrantRequestEntityDecorator;
    private final DefaultAuthorizationCodeTokenResponseClient delegator = new DefaultAuthorizationCodeTokenResponseClient();
    private final RestTemplate restTemplate;

    private static final Predicate<OAuth2AuthorizationCodeGrantRequest> IS_INSTAGRAM = request ->
            request.getClientRegistration().getRegistrationId().contains(INSTAGRAM.getRegistrationId());

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        if (IS_INSTAGRAM.test(authorizationCodeGrantRequest))
            return getInstagramTokenResponse(authorizationCodeGrantRequest);
        return delegator.getTokenResponse(authorizationCodeGrantRequest);
    }

    @Override
    public void afterPropertiesSet() {
        delegator.setRequestEntityConverter(oAuth2AuthorizationCodeGrantRequestEntityDecorator);
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
    }

    private OAuth2AccessTokenResponse getInstagramTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        RequestEntity<?> request = oAuth2AuthorizationCodeGrantRequestEntityDecorator.convert(authorizationGrantRequest);
        InstagramAccessToken body = restTemplate.exchange(request, InstagramAccessToken.class).getBody();

        Map<String, Object> additionalParameters = Map.of("access_token", body.accessToken(), "user_id", body.userId());

        return OAuth2AccessTokenResponse
                .withToken(body.accessToken())
                .tokenType(BEARER)
                .additionalParameters(additionalParameters)
                .build();
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private record InstagramAccessToken(@NotNull String accessToken, @NotNull String userId) {

    }
}
