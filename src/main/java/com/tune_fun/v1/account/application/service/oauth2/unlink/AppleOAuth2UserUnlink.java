package com.tune_fun.v1.account.application.service.oauth2.unlink;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AppleOAuth2UserUnlink implements OAuth2UserUnlinkTemplate {

    private static final String URL = "https://appleid.apple.com/auth/revocation";
    private final RestTemplate restTemplate;

    @Override
    public void unlink(String accessToken) {
        restTemplate.postForObject(URL, null, String.class);
    }
}
