package com.tune_fun.v1.account.application.port.output.oauth2;

import com.tune_fun.v1.account.domain.state.RegisteredOAuth2Account;

import java.util.Optional;

public interface LoadOAuth2AccountPort {
    Optional<RegisteredOAuth2Account> findByOAuth2ProviderAndEmail(final String oauth2Provider, final String email);
}
