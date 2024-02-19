package com.tune_fun.v1.otp.domain.behavior;

public record SendOtp(
        String email,
        String nickname,
        String token
) {
}
