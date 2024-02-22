package com.tune_fun.v1.otp.domain.state;

public record CurrentDecryptedOtp(
        String username,
        String otpType,
        String token
) {
}
