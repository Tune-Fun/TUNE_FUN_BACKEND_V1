package com.tune_fun.v1.account.domain.value;

import com.tune_fun.v1.common.response.BasePayload;

import java.util.Set;

public record RegisterResult(
        String username,
        Set<String> roles,
        String accessToken,
        String refreshToken
) implements BasePayload {
}
