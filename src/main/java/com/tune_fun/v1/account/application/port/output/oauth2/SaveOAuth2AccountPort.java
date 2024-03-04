package com.tune_fun.v1.account.application.port.output.oauth2;

import com.tune_fun.v1.account.domain.behavior.SaveOAuth2Account;

public interface SaveOAuth2AccountPort {
    void saveOAuth2Account(final SaveOAuth2Account behavior);
}
