package com.tune_fun.v1.otp.domain.state;

import com.tune_fun.v1.common.response.BasePayload;

public record VerifyResult(String accessToken, String refreshToken) implements BasePayload {
}
