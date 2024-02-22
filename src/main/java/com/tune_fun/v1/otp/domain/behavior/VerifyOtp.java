package com.tune_fun.v1.otp.domain.behavior;

public record VerifyOtp(
        String username,
        String otpType,
        String otp
) {
}
