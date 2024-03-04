package com.tune_fun.v1.account.domain.behavior;

public record SaveOAuth2Account(
        String email,
        String nickname,
        String oauth2Provider,
        String username
) {
}
