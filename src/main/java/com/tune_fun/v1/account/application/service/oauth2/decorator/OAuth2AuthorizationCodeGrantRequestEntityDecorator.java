package com.tune_fun.v1.account.application.service.oauth2.decorator;

import com.tune_fun.v1.common.helper.AppleOAuth2ClientSecretHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

import static com.tune_fun.v1.account.domain.value.oauth2.OAuth2Provider.APPLE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_SECRET;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthorizationCodeGrantRequestEntityDecorator extends OAuth2AuthorizationCodeGrantRequestEntityConverter {

    private final AppleOAuth2ClientSecretHelper appleOAuth2ClientSecretHelper;


    @Override
    @SuppressWarnings("unchecked")
    public RequestEntity<?> convert(@NotNull OAuth2AuthorizationCodeGrantRequest request) {
        RequestEntity<?> entity = super.convert(request);

        if (entity == null || entity.getBody() == null)
            throw new IllegalStateException("Conversion resulted in a null entity or body.");

        String registrationId = request.getClientRegistration().getRegistrationId();
        if (!registrationId.contains(APPLE.getRegistrationId())) return entity;

        MultiValueMap<String, String> params;
        try {
            params = (MultiValueMap<String, String>) entity.getBody();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Body of request entity is not of expected type MultiValueMap<String, String>.");
        }

        String clientSecret;
        try {
            clientSecret = appleOAuth2ClientSecretHelper.createAppleClientSecret();
        } catch (IOException e) {
            throw new IllegalStateException("Error while creating Apple client secret.");
        }

        params.set(CLIENT_SECRET, clientSecret);

        return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
    }

}
