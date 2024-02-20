package com.tune_fun.v1.otp.domain.state;

import com.tune_fun.v1.otp.adapter.output.persistence.OtpType;

public record CurrentDecryptedOtp(
        String username,
        OtpType otpType,
        String token
) {
}
