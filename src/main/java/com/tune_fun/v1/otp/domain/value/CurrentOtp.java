package com.tune_fun.v1.otp.domain.value;

public record CurrentOtp(
        String username,
        String otpType,
        String token
) {
}
