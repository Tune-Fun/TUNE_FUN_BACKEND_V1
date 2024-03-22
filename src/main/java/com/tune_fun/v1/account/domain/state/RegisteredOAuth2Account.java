package com.tune_fun.v1.account.domain.state;

public record RegisteredOAuth2Account(
        String email,
        String oauth2Provider
) {
}
