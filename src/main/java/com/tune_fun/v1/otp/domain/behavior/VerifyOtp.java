package com.tune_fun.v1.otp.domain.behavior;

public record VerifyOtp(
        String username,
        OtpType otpType,
        String otp
) {
}
