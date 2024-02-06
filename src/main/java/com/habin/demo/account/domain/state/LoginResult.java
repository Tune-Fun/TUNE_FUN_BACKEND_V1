package com.habin.demo.account.domain.state;

import com.habin.demo.common.response.BasePayload;

import java.util.List;

public record LoginResult(
        String username,
        List<String> roles,
        String accessToken,
        String refreshToken
) implements BasePayload {
}
