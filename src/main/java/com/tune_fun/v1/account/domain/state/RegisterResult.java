package com.tune_fun.v1.account.domain.state;

import com.tune_fun.v1.common.response.BasePayload;

import java.util.List;
import java.util.Set;

public record RegisterResult(
        String username,
        Set<String> roles,
        String accessToken,
        String refreshToken
) implements BasePayload {
}
