package com.tune_fun.v1.otp.application.port.output;

public interface DeleteOtpPort {
    void expire(final String otpType, final String username);
}
