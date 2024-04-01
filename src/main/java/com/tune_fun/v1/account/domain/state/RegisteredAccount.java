package com.tune_fun.v1.account.domain.state;

import com.tune_fun.v1.account.domain.state.oauth2.RegisteredOAuth2Account;

import java.util.List;
import java.util.Set;

public record RegisteredAccount(
        String username,
        String password,
        Set<String> roles,
        List<RegisteredOAuth2Account> oauth2Accounts
) {
}
