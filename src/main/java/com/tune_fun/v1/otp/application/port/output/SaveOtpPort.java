package com.tune_fun.v1.otp.application.port.output;

import com.tune_fun.v1.otp.domain.behavior.SaveOtp;
import com.tune_fun.v1.otp.domain.state.CurrentOtp;

public interface SaveOtpPort {
    CurrentOtp saveOtp(final SaveOtp saveOtp) throws Exception;
}
