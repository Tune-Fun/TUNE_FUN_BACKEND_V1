package com.tune_fun.v1.account.domain.state.oauth2;

public record RegisteredOAuth2Account(
        String email,
        String nickname,
        String oauth2Provider,
        boolean enabled
) {
}
