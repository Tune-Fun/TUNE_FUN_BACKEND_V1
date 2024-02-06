package com.habin.demo.account.domain.state;

import com.habin.demo.common.response.BasePayload;

public record ReissuedAccessToken(
        String accessToken
) implements BasePayload {
}
