package com.tune_fun.v1.account.domain.state;

import java.util.List;

public record RegisteredAccount(
        String username,
        String password,
        List<String> roles
) {
}
