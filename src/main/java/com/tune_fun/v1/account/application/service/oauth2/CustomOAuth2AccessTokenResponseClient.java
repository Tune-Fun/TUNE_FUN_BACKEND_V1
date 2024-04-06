package com.tune_fun.v1.account.application.service.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.tune_fun.v1.account.domain.state.oauth2.OAuth2Provider.INSTAGRAM;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

/**
 * @see <a href="https://velog.io/@shdrnrhd113/인스타그램-소셜-로그인-구현하기">인스타그램 소셜 로그인 구현하기</a>
 */
@Component
@RequiredArgsConstructor
public class CustomOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private final Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter =
            new OAuth2AuthorizationCodeGrantRequestEntityConverter();

    private final DefaultAuthorizationCodeTokenResponseClient delegator = new DefaultAuthorizationCodeTokenResponseClient();

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        if (authorizationCodeGrantRequest.getClientRegistration().getRegistrationId()
                .contains(INSTAGRAM.getRegistrationId()))
            return getInstagramTokenResponse(authorizationCodeGrantRequest);

        return delegator.getTokenResponse(authorizationCodeGrantRequest);
    }

    public OAuth2AccessTokenResponse getInstagramTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<?> request = this.requestEntityConverter.convert(authorizationGrantRequest);

        ResponseEntity<InstagramAccessToken> response = restTemplate.exchange(request, InstagramAccessToken.class);
        InstagramAccessToken body = response.getBody();

        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put("access_token", body.accessToken());
        additionalParameters.put("user_id", body.userId());

        return OAuth2AccessTokenResponse
                .withToken(body.accessToken())
                .tokenType(BEARER)
                .additionalParameters(additionalParameters)
                .build();
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private record InstagramAccessToken(String accessToken, String userId) {
    }
}
