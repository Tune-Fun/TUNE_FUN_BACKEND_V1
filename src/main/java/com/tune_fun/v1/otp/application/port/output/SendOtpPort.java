package com.tune_fun.v1.otp.application.port.output;

import com.tune_fun.v1.otp.domain.behavior.SendOtp;

public interface SendOtpPort {
    void sendOtp(final SendOtp sendOtp) throws Exception;
}
