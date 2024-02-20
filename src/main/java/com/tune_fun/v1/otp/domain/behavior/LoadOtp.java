package com.tune_fun.v1.otp.domain.behavior;

import com.tune_fun.v1.otp.adapter.output.persistence.OtpType;

public record LoadOtp(
        String username,
        OtpType otpType
) {
}
