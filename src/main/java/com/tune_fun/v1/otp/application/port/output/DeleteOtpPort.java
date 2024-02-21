package com.tune_fun.v1.otp.application.port.output;

import com.tune_fun.v1.otp.adapter.output.persistence.OtpType;

public interface DeleteOtpPort {
    void expire(final OtpType otpType, final String username);
}
