package com.tune_fun.v1.account.domain.state;

import com.tune_fun.v1.common.response.BasePayload;

public record ReissuedAccessToken(
        String accessToken
) implements BasePayload {
}
