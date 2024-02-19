package com.tune_fun.v1.otp.application.port.output;

import com.tune_fun.v1.otp.domain.state.CurrentOtp;
import com.tune_fun.v1.otp.domain.behavior.SaveOtp;

public interface SaveOtpPort {
    CurrentOtp saveOtp(final SaveOtp saveOtp) throws Exception;
}
