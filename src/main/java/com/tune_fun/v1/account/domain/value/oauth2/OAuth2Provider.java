package com.tune_fun.v1.account.domain.value.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuth2Provider {
    GOOGLE("google"), APPLE("apple"), INSTAGRAM("instagram");

    private final String registrationId;

    public static OAuth2Provider fromRegistrationId(String registrationId) {
        for (OAuth2Provider provider : OAuth2Provider.values())
            if (provider.getRegistrationId().equals(registrationId)) return provider;

        return null;
    }
}
