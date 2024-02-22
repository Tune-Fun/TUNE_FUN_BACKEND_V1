package com.tune_fun.v1.otp.domain.behavior;

public record SaveOtp(
        String username,
        String otpType
) {
}
