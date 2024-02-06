package com.tune_fun.v1.account.domain.state;

import com.tune_fun.v1.common.response.BasePayload;

import java.util.List;

public record RegisterResult(
        String username,
        List<String> roles,
        String accessToken,
        String refreshToken
) implements BasePayload {
}
