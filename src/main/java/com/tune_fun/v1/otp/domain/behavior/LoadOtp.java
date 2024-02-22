package com.tune_fun.v1.otp.domain.behavior;

public record LoadOtp(
        String username,
        String otpType
) {
}
