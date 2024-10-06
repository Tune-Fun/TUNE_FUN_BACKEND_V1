package com.tune_fun.v1.account.domain.value;

import com.tune_fun.v1.common.response.BasePayload;

import java.util.Set;

public record LoginResult(
        Long id,
        String username,
        String nickname,
        String email,
        Set<Role> roles,
        String accessToken,
        String refreshToken
) implements BasePayload {
}
